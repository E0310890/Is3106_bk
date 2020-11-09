package com.is3106.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.bytebuddy.build.Plugin.Engine.Source.Empty;

public class BaseResponseDto<T> {
	
	private HttpStatus httpStatus;
	private String message;
	private T data;

    public static <T> ResponseEntity<BaseResponseDto<T>> success(String message, T data){
    	BaseResponseDto<T> restResponseDto = new BaseResponseDto<>();
        restResponseDto.httpStatus = HttpStatus.OK;
        restResponseDto.message = message;
        restResponseDto.data = data;
        return ResponseEntity.ok(restResponseDto);
    }
    

	public static ResponseEntity<BaseResponseDto<?>> success(String message){
    	BaseResponseDto<Empty> restResponseDto = new BaseResponseDto<>();
        restResponseDto.httpStatus = HttpStatus.OK;
        restResponseDto.message = message;
        return ResponseEntity.ok(restResponseDto);
    }

    public static <T> ResponseEntity<BaseResponseDto<T>> fail(String message, T data){
    	HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    	BaseResponseDto<T> restResponseDto = new BaseResponseDto<>();
        restResponseDto.httpStatus = httpStatus;
        restResponseDto.message = message;
        restResponseDto.data = data;
        return new ResponseEntity<>(restResponseDto, httpStatus);
    }
    
	public static ResponseEntity<BaseResponseDto<?>> fail(String message){
    	BaseResponseDto<Empty> restResponseDto = new BaseResponseDto<>();
        restResponseDto.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        restResponseDto.message = message;
        return ResponseEntity.ok(restResponseDto);
    }
    
    public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
