package id.co.singgih.springboot.mongodb.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T extends Object> {
	@JsonProperty("status")
	private String status = "";
	@JsonProperty("error_message")
	private String errorMessage = "";
	@JsonProperty("files")
	private List<T> files;
	@JsonProperty("file")
	private T file;
}