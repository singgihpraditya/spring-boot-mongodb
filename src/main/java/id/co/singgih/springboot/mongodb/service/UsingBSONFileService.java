package id.co.singgih.springboot.mongodb.service;

import java.util.Base64;
import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import id.co.singgih.springboot.mongodb.entity.SmallFile;
import id.co.singgih.springboot.mongodb.repository.SmallFileRepository;
import id.co.singgih.springboot.mongodb.response.GetFileBase64Response;
import id.co.singgih.springboot.mongodb.util.Constants;

@Service
public class UsingBSONFileService {
	private Logger logger = LoggerFactory.getLogger(Constants.SERVICE_NAME);

	@Autowired
	private SmallFileRepository fileRepository;

	public String saveFile(String hashCode, String createdBy, MultipartFile multipartFile) throws Exception {
		String fileName = multipartFile.getOriginalFilename();
		Long size = multipartFile.getSize();
		logger.debug(hashCode + "Try to save file :{}, size :{}", fileName,size);
		
		SmallFile file = new SmallFile();
		file.setCreatedBy(createdBy);
		file.setCreatedDate(new Date());
		file.setFileName(fileName);
		file.setContentType(multipartFile.getContentType());
		file.setFileSize(size);
		file.setImage(new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
		file = fileRepository.insert(file);
		String id = file.getId();
		logger.debug(hashCode + "Save file :{}, id :{} success", fileName,id);
		return id;
	}

	public GetFileBase64Response getFileBase64ById(String hashCode, String id) throws Exception {
		logger.debug(hashCode + "Get file with id :{} ", id);
		SmallFile file =  fileRepository.findById(id).get();
		String base64Image = Base64.getEncoder().encodeToString(file.getImage().getData());
		GetFileBase64Response getFileBase64Response = new GetFileBase64Response();
		getFileBase64Response.setId(id);
		getFileBase64Response.setCreatedBy(file.getCreatedBy());
		getFileBase64Response.setCreatedDate(file.getCreatedDate());
		getFileBase64Response.setFileName(file.getFileName());
		getFileBase64Response.setFileSize(file.getFileSize());
		getFileBase64Response.setBase64image(base64Image);
		getFileBase64Response.setContentType(file.getContentType());
		return getFileBase64Response;
	}
}
