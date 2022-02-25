package id.co.singgih.springboot.mongodb.response;

import java.util.Date;

import lombok.Data;

@Data
public class UploadFileResponse {
	private String id;
	private String fileName;
	private Long fileSize;
	private String createdBy;
	private Date createdDate;
	private String contentType;
}
