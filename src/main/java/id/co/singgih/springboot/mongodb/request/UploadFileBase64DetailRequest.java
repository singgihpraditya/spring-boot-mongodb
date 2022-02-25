package id.co.singgih.springboot.mongodb.request;

import lombok.Data;

@Data
public class UploadFileBase64DetailRequest {
	private String fileName;
	private String contentType;
	private String base64File;
}
