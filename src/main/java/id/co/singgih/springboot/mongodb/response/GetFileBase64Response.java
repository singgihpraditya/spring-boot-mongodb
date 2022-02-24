package id.co.singgih.springboot.mongodb.response;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class GetFileBase64Response {
	@Id
	private String id;
	private String fileName;
	private Long fileSize;
	private String createdBy;
	private Date createdDate;
	private String contentType;
	private String base64image;
}
