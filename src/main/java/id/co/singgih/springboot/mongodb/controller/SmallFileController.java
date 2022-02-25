package id.co.singgih.springboot.mongodb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import id.co.singgih.springboot.mongodb.request.UploadFileBase64Request;
import id.co.singgih.springboot.mongodb.response.BaseResponse;
import id.co.singgih.springboot.mongodb.response.GetFileBase64Response;
import id.co.singgih.springboot.mongodb.response.UploadFileResponse;
import id.co.singgih.springboot.mongodb.service.UsingBSONFileService;
import id.co.singgih.springboot.mongodb.util.Constants;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/small-file")
@Api(value = "Small File API", description = "Operations pertaining to Upload / Get File API < 16MB", tags = "SmallFileAPI")
public class SmallFileController {
	@Autowired
	private UsingBSONFileService fileService;

	private Logger logger = LoggerFactory.getLogger(Constants.SERVICE_NAME);

	private String getHashCodeNumber() {
		int min = 100000;
		int max = 999999;
		return "[" + (int) ((Math.random() * (max - min)) + min) + "] ";
	}

	@PostMapping("/upload")
	public ResponseEntity<Object>  uploadFile(@RequestParam("created-by")String createdBy, @RequestParam("files") MultipartFile[] multipartFiles) throws IOException {
		long startMillis = System.currentTimeMillis();
		String hashCode = getHashCodeNumber();

		logger.debug(hashCode + "---- Start Upload File ----");
		
		BaseResponse<UploadFileResponse> response = new BaseResponse<UploadFileResponse>();
	
		try {
			List<UploadFileResponse> uploadFileResponses = fileService.saveFile(hashCode, createdBy, multipartFiles);
			response.setFiles(uploadFileResponses);
			response.setStatus(Constants.SUCCESS);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch(Exception ex) {
			logger.error(hashCode + "Failed upload file : "+ex.getMessage());
			response.setStatus(Constants.FAILED);
			response.setErrorMessage(ex.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		finally {
			long execTime = (System.currentTimeMillis() - startMillis);
			logger.debug(hashCode + "---- End Upload File, Executed in : " + execTime + " ms ----");
		}
	}
	
	@PostMapping("/upload-base64")
	public ResponseEntity<Object>  uploadBase64File(@RequestBody UploadFileBase64Request uploadFileBase64Request) throws IOException {
		long startMillis = System.currentTimeMillis();
		String hashCode = getHashCodeNumber();

		logger.debug(hashCode + "---- Start Upload File ----");
		
		BaseResponse<UploadFileResponse> response = new BaseResponse<UploadFileResponse>();
		try {
			List<UploadFileResponse> uploadFileResponses = fileService.saveFile(hashCode, uploadFileBase64Request);
			response.setFiles(uploadFileResponses);
			response.setStatus(Constants.SUCCESS);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch(Exception ex) {
			logger.error(hashCode + "Failed upload file : "+ex.getMessage());
			response.setStatus(Constants.FAILED);
			response.setErrorMessage(ex.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		finally {
			long execTime = (System.currentTimeMillis() - startMillis);
			logger.debug(hashCode + "---- End Upload File, Executed in : " + execTime + " ms ----");
		}
	}
	
	
	@GetMapping("/get-base64/{id}")
	public ResponseEntity<Object> getBase64(@PathVariable String id) {
	    long startMillis = System.currentTimeMillis();
		String hashCode = getHashCodeNumber();

		logger.debug(hashCode + "---- Get Upload File (BASE64) ----");
		
		BaseResponse<GetFileBase64Response> response = new BaseResponse<GetFileBase64Response>();
		
		try {
			GetFileBase64Response getFileBase64Response = fileService.getFileBase64ById(hashCode, id);
			response.setFile(getFileBase64Response);
			response.setStatus(Constants.SUCCESS);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch(Exception ex) {
			logger.error(hashCode + "Failed get file : "+ex.getMessage());
			response.setStatus(Constants.FAILED);
			response.setErrorMessage(ex.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		finally {
			long execTime = (System.currentTimeMillis() - startMillis);
			logger.debug(hashCode + "---- End Get Upload File (BASE64), Executed in : " + execTime + " ms ----");
		}
	}
}