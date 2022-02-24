package id.co.singgih.springboot.mongodb.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;

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

import id.co.singgih.springboot.mongodb.request.UploadFileBase64Request;
import id.co.singgih.springboot.mongodb.response.GetFileBase64Response;
import id.co.singgih.springboot.mongodb.util.Constants;

@Service
public class UsingGFSFileService {
	private Logger logger = LoggerFactory.getLogger(Constants.SERVICE_NAME);

	@Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

	public String saveFile(String hashCode, String createdBy, MultipartFile multipartFile) throws Exception {
		String fileName = multipartFile.getOriginalFilename();
		Long size = multipartFile.getSize();
		logger.debug(hashCode + "Try to save file :{}, size :{}", fileName,size);
		
		DBObject metaData = new BasicDBObject();
		metaData.put("createdBy", createdBy);
		metaData.put("fileName", fileName);
		metaData.put("fileSize", size);
		metaData.put("contentType", multipartFile.getContentType());
		metaData.put("createdDate", new Date());
		ObjectId objectId = gridFsTemplate.store(multipartFile.getInputStream(), multipartFile.getName(), multipartFile.getContentType(), metaData);

		String id = objectId.toString();
		logger.debug(hashCode + "Save file :{}, id :{} success", fileName,id);
		return id;
	}

	public GetFileBase64Response getFileBase64ById(String hashCode, String id) throws Exception {
		logger.debug(hashCode + "Get file with id :{} ", id);
		GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
		InputStream inputStream = operations.getResource(file).getInputStream();
		byte[] byteImage = StreamUtils.copyToByteArray(inputStream);
		String base64Image = Base64.getEncoder().encodeToString(byteImage);
		GetFileBase64Response getFileBase64Response = new GetFileBase64Response();
		getFileBase64Response.setId(id);
		getFileBase64Response.setContentType(file.getMetadata().get("contentType")==null?null:file.getMetadata().get("contentType").toString());
		getFileBase64Response.setCreatedBy(file.getMetadata().get("createdBy")==null?null:file.getMetadata().get("createdBy").toString());
		getFileBase64Response.setCreatedDate(file.getMetadata().get("createdDate")==null?null:(Date)file.getMetadata().get("createdDate"));
		getFileBase64Response.setFileName(file.getMetadata().get("fileName")==null?null:file.getMetadata().get("fileName").toString());
		getFileBase64Response.setFileSize(file.getMetadata().get("fileSize")==null?null:Long.parseLong(file.getMetadata().get("fileSize").toString()));
		getFileBase64Response.setBase64image(base64Image);
		return getFileBase64Response;
	}

	public String saveFile(String hashCode, UploadFileBase64Request uploadFileBase64Request) {
		String fileName = uploadFileBase64Request.getFileName();
		Long size = uploadFileBase64Request.getFileSize();
		logger.debug(hashCode + "Try to save file :{}, size :{}", fileName,size);
		
		DBObject metaData = new BasicDBObject();
		metaData.put("createdBy", uploadFileBase64Request.getCreatedBy());
		metaData.put("fileName", fileName);
		metaData.put("fileSize", size);
		metaData.put("contentType", uploadFileBase64Request.getContentType());
		metaData.put("createdDate", new Date());
		byte[] byteImage = Base64.getDecoder().decode(uploadFileBase64Request.getBase64image());
		InputStream inputStream = new ByteArrayInputStream(byteImage);
		ObjectId objectId = gridFsTemplate.store(inputStream, uploadFileBase64Request.getFileName(), uploadFileBase64Request.getContentType(), metaData);

		String id = objectId.toString();
		logger.debug(hashCode + "Save file :{}, id :{} success", fileName,id);
		return id;
	}
}
