package id.co.singgih.springboot.mongodb.request;

import java.util.List;

import lombok.Data;

@Data
public class UploadFileBase64Request {
	private String createdBy;
	private List<UploadFileBase64DetailRequest> files;
}
