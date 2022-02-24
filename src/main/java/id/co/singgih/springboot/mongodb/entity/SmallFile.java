package id.co.singgih.springboot.mongodb.entity;

import java.util.Date;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "small-file")
@Data
public class SmallFile {
	@Id
	private String id;
	private String fileName;
	private Long fileSize;
	private String contentType;
	private Binary image;
	private String createdBy;
	private Date createdDate;
}
