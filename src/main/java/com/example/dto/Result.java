package com.example.dto;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public final class Result<T> {

	private int status;
	private boolean success;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private HashMap<String, List<String>> errors;
	
	private Result(HttpStatus status,boolean success,String message,T data) {
		this.status=status.value();
		this.success=success;
		this.message=message;
		this.data=data;
	}
	private Result(HttpStatus status,boolean success,String message,T data,HashMap<String, List<String>> map) {
		this.status=status.value();
		this.success=success;
		this.message=message;
		this.data=data;
		this.errors=map;
	}
	
	public static <T> Result<T> success(HttpStatus status,String message){
		return new Result<T>(status, true, message, null);
	}
	
	public static <T> Result<T> successData(HttpStatus status,String message,T data){
		return new Result<T>(status, true, message, data);
	}
	
	public static <T> Result<T> error(HttpStatus status,String message){
		return new Result<T>(status, false, message, null);
	}
	
	public static <T> Result<T> notValid(HashMap<String, List<String>> map){
		return new Result<T>(HttpStatus.BAD_REQUEST, false, null, null,map);
	}
}
