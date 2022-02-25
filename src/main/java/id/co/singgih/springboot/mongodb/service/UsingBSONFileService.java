package id.co.singgih.springboot.mongodb.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import id.co.singgih.springboot.mongodb.entity.SmallFile;
import id.co.singgih.springboot.mongodb.repository.SmallFileRepository;
import id.co.singgih.springboot.mongodb.request.UploadFileBase64DetailRequest;
import id.co.singgih.springboot.mongodb.request.UploadFileBase64Request;
import id.co.singgih.springboot.mongodb.response.GetFileBase64Response;
import id.co.singgih.springboot.mongodb.response.UploadFileResponse;
import id.co.singgih.springboot.mongodb.util.Constants;

@Service
public class UsingBSONFileService {
	private Logger logger = LoggerFactory.getLogger(Constants.SERVICE_NAME);

	@Autowired
	private SmallFileRepository fileRepository;

	public List<UploadFileResponse> saveFile(String hashCode, String createdBy, MultipartFile[] multipartFiles) throws Exception {
		List<UploadFileResponse> uploadFileResponses = new ArrayList<UploadFileResponse>();
		for (MultipartFile multipartFile : multipartFiles) {
			UploadFileResponse uploadFileResponse = new UploadFileResponse();
			String fileName = multipartFile.getOriginalFilename();
			Long size = multipartFile.getSize();
			logger.debug(hashCode + "Try to save file :{}, size :{}", fileName,size);
			
			SmallFile file = new SmallFile();
			file.setCreatedBy(createdBy);
			file.setCreatedDate(new Date());
			file.setFileName(fileName);
			file.setContentType(multipartFile.getContentType());
			file.setFileSize(size);
			
			file.setBinaryFile(new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
			file = fileRepository.insert(file);
			String id = file.getId();
			logger.debug(hashCode + "Save file :{}, id :{} success", fileName,id);
			uploadFileResponse.setId(id);
			uploadFileResponse.setCreatedBy(file.getCreatedBy());
			uploadFileResponse.setCreatedDate(file.getCreatedDate());
			uploadFileResponse.setFileName(file.getFileName());
			uploadFileResponse.setFileSize(file.getFileSize());
			uploadFileResponse.setContentType(file.getContentType());
			
			uploadFileResponses.add(uploadFileResponse);
		}
		return uploadFileResponses;
	}

	public GetFileBase64Response getFileBase64ById(String hashCode, String id) throws Exception {
		logger.debug(hashCode + "Get file with id :{} ", id);
		SmallFile file =  fileRepository.findById(id).get();
		String base64File = Base64.getEncoder().encodeToString(file.getBinaryFile().getData());
		GetFileBase64Response getFileBase64Response = new GetFileBase64Response();
		getFileBase64Response.setId(id);
		getFileBase64Response.setCreatedBy(file.getCreatedBy());
		getFileBase64Response.setCreatedDate(file.getCreatedDate());
		getFileBase64Response.setFileName(file.getFileName());
		getFileBase64Response.setFileSize(file.getFileSize());
		getFileBase64Response.setBase64File(base64File);
		getFileBase64Response.setContentType(file.getContentType());
		return getFileBase64Response;
	}

	public List<UploadFileResponse> saveFile(String hashCode, UploadFileBase64Request uploadFileBase64Request) {
		List<UploadFileResponse> uploadFileResponses = new ArrayList<UploadFileResponse>();
		List<UploadFileBase64DetailRequest> base64DetailRequests = uploadFileBase64Request.getFiles();
		for (UploadFileBase64DetailRequest base64DetailRequest : base64DetailRequests) {
			UploadFileResponse uploadFileResponse = new UploadFileResponse();
			
			byte[] byteArrayFile = Base64.getDecoder().decode(base64DetailRequest.getBase64File());
			logger.debug(hashCode + "Try to save file :{}, size :{}", base64DetailRequest.getFileName(), byteArrayFile.length);
			
			SmallFile file = new SmallFile();
			file.setFileSize(new Long(byteArrayFile.length));
			file.setCreatedBy(uploadFileBase64Request.getCreatedBy());
			file.setCreatedDate(new Date());
			file.setFileName(base64DetailRequest.getFileName());
			file.setContentType(base64DetailRequest.getContentType());
			file.setBinaryFile(new Binary(BsonBinarySubType.BINARY, byteArrayFile));
			file = fileRepository.insert(file);
			String id = file.getId();
			logger.debug(hashCode + "Save file :{}, id :{} success", base64DetailRequest.getFileName(),id);
			
			uploadFileResponse.setId(id);
			uploadFileResponse.setCreatedBy(file.getCreatedBy());
			uploadFileResponse.setCreatedDate(file.getCreatedDate());
			uploadFileResponse.setFileName(file.getFileName());
			uploadFileResponse.setFileSize(file.getFileSize());
			uploadFileResponse.setContentType(file.getContentType());
			
			uploadFileResponses.add(uploadFileResponse);
		}
	
		return uploadFileResponses;
	}
}
