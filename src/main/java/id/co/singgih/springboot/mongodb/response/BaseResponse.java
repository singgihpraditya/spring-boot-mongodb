package id.co.singgih.springboot.mongodb.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BaseResponse<T extends Object> {
	@JsonProperty("status")
	private String status = "";
	@JsonProperty("error_message")
	private String errorMessage  = "";
	@JsonProperty("payload")
	private T payload;
}