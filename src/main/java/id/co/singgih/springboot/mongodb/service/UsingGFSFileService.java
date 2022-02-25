package id.co.singgih.springboot.mongodb.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

import id.co.singgih.springboot.mongodb.request.UploadFileBase64DetailRequest;
import id.co.singgih.springboot.mongodb.request.UploadFileBase64Request;
import id.co.singgih.springboot.mongodb.response.GetFileBase64Response;
import id.co.singgih.springboot.mongodb.response.UploadFileResponse;
import id.co.singgih.springboot.mongodb.util.Constants;

@Service
public class UsingGFSFileService {
	private Logger logger = LoggerFactory.getLogger(Constants.SERVICE_NAME);

	@Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

	public List<UploadFileResponse>  saveFile(String hashCode, String createdBy, MultipartFile[] multipartFiles) throws Exception {
		List<UploadFileResponse> uploadFileResponses = new ArrayList<UploadFileResponse>();
		for (MultipartFile multipartFile : multipartFiles) {
			UploadFileResponse uploadFileResponse = new UploadFileResponse();
			String fileName = multipartFile.getOriginalFilename();
			Long size = multipartFile.getSize();
			logger.debug(hashCode + "Try to save file :{}, size :{}", fileName,size);
			Date createdDate = new Date();
			DBObject metaData = new BasicDBObject();
			metaData.put("createdBy", createdBy);
			metaData.put("fileName", fileName);
			metaData.put("fileSize", size);
			metaData.put("contentType", multipartFile.getContentType());
			metaData.put("createdDate", createdDate);
			ObjectId objectId = gridFsTemplate.store(multipartFile.getInputStream(), multipartFile.getName(), multipartFile.getContentType(), metaData);
	
			String id = objectId.toString();
			logger.debug(hashCode + "Save file :{}, id :{} success", fileName,id);
			uploadFileResponse.setId(id);
			uploadFileResponse.setCreatedBy(createdBy);
			uploadFileResponse.setCreatedDate(createdDate);
			uploadFileResponse.setFileName(fileName);
			uploadFileResponse.setFileSize(size);
			uploadFileResponse.setContentType(multipartFile.getContentType());
			
			uploadFileResponses.add(uploadFileResponse);
		}
		return uploadFileResponses;
	}

	public GetFileBase64Response getFileBase64ById(String hashCode, String id) throws Exception {
		logger.debug(hashCode + "Get file with id :{} ", id);
		GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
		InputStream inputStream = operations.getResource(file).getInputStream();
		byte[] byteArrayFile = StreamUtils.copyToByteArray(inputStream);
		String base64File = Base64.getEncoder().encodeToString(byteArrayFile);
		GetFileBase64Response getFileBase64Response = new GetFileBase64Response();
		getFileBase64Response.setId(id);
		getFileBase64Response.setContentType(file.getMetadata().get("contentType")==null?null:file.getMetadata().get("contentType").toString());
		getFileBase64Response.setCreatedBy(file.getMetadata().get("createdBy")==null?null:file.getMetadata().get("createdBy").toString());
		getFileBase64Response.setCreatedDate(file.getMetadata().get("createdDate")==null?null:(Date)file.getMetadata().get("createdDate"));
		getFileBase64Response.setFileName(file.getMetadata().get("fileName")==null?null:file.getMetadata().get("fileName").toString());
		getFileBase64Response.setFileSize(file.getMetadata().get("fileSize")==null?null:Long.parseLong(file.getMetadata().get("fileSize").toString()));
		getFileBase64Response.setBase64File(base64File);
		return getFileBase64Response;
	}

	public List<UploadFileResponse>  saveFile(String hashCode, UploadFileBase64Request uploadFileBase64Request) {
		List<UploadFileResponse> uploadFileResponses = new ArrayList<UploadFileResponse>();
		List<UploadFileBase64DetailRequest> base64DetailRequests = uploadFileBase64Request.getFiles();
		for (UploadFileBase64DetailRequest uploadFileBase64DetailRequest : base64DetailRequests) {
			UploadFileResponse uploadFileResponse = new UploadFileResponse();
			
			String fileName = uploadFileBase64DetailRequest.getFileName();
			byte[] byteArrayFile = Base64.getDecoder().decode(uploadFileBase64DetailRequest.getBase64File());
			logger.debug(hashCode + "Try to save file :{}, size :{}", fileName, byteArrayFile.length);
			Date createdDate = new Date();
			DBObject metaData = new BasicDBObject();
			metaData.put("createdBy", uploadFileBase64Request.getCreatedBy());
			metaData.put("fileName", fileName);
			metaData.put("fileSize", byteArrayFile.length);
			metaData.put("contentType", uploadFileBase64DetailRequest.getContentType());
			metaData.put("createdDate", new Date());
			
			InputStream inputStream = new ByteArrayInputStream(byteArrayFile);
			ObjectId objectId = gridFsTemplate.store(inputStream, uploadFileBase64DetailRequest.getFileName(), uploadFileBase64DetailRequest.getContentType(), metaData);

			String id = objectId.toString();
			logger.debug(hashCode + "Save file :{}, id :{} success", fileName,id);
			
			uploadFileResponse.setId(id);
			uploadFileResponse.setCreatedBy(uploadFileBase64Request.getCreatedBy());
			uploadFileResponse.setCreatedDate(createdDate);
			uploadFileResponse.setFileName(fileName);
			uploadFileResponse.setFileSize(new Long(byteArrayFile.length));
			uploadFileResponse.setContentType(uploadFileBase64DetailRequest.getContentType());
			
			uploadFileResponses.add(uploadFileResponse);
		}

		return uploadFileResponses;
	}
}
