package id.co.singgih.springboot.mongodb.request;

import lombok.Data;

@Data
public class UploadFileBase64Request {
	private String fileName;
	private Long fileSize;
	private String createdBy;
	private String contentType;
	private String base64image;
}