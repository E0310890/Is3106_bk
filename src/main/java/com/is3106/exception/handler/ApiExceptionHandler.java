package com.is3106.exception.handler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.amazonaws.services.organizations.model.ConstraintViolationException;
import com.is3106.exception.ApiRequestException;
import com.is3106.exception.EmailServerException;
import com.is3106.exception.FileServerException;
import com.is3106.exception.InvalidCredentialsException;
import com.is3106.exception.JsonMappingException;
import com.is3106.exception.JwtException;

import io.jsonwebtoken.MalformedJwtException;


@ControllerAdvice
public class ApiExceptionHandler {
	
	@ExceptionHandler(value = { DataIntegrityViolationException.class, ConstraintViolationException.class, SQLIntegrityConstraintViolationException.class })
	public ResponseEntity<Object> handleApiRequestException(RuntimeException ex) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);

	}
	

	@ExceptionHandler(value = { ApiRequestException.class })
	public ResponseEntity<Object> handleApiRequestException(ApiRequestException ex) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);

	}

	@ExceptionHandler(value = { FileServerException.class })
	public ResponseEntity<Object> handleApiRequestException(FileServerException ex) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);

	}

	@ExceptionHandler(value = { NoSuchElementException.class })
	public ResponseEntity<Object> handleApiRequestException(NoSuchElementException ex) {

		HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));
		
		ex.printStackTrace();

		return new ResponseEntity<>(apiException, httpStatus);

	}

	// handle: when JsonMapper function fails to map given object
	@ExceptionHandler(value = { JsonMappingException.class })
	public ResponseEntity<Object> handleApiRequestException(JsonMappingException ex) {

		HttpStatus httpStatus = HttpStatus.PRECONDITION_FAILED;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);

	}

	// handle: when requesting of unimplemented api method.
	@ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<Object> handleApiRequestException(HttpRequestMethodNotSupportedException ex) {

		HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);

	}

	// handle: when providing wrong type to api method.
	@ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleApiRequestException(MethodArgumentTypeMismatchException ex) {

		HttpStatus httpStatus = HttpStatus.PRECONDITION_FAILED;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);

	}

	@ExceptionHandler(value = { MissingServletRequestPartException.class })
	public ResponseEntity<Object> handleApiRequestException(MissingServletRequestPartException ex) {

		HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);

	}

	// handle: when parameter of api method not satisfied or not found/given.
	@ExceptionHandler(value = { MissingServletRequestParameterException.class })
	public ResponseEntity<Object> handleApiRequestException(MissingServletRequestParameterException ex) {

		HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);

	}
	
	// handle: when the current request gave a parameter that is not a multipart request
	@ExceptionHandler(value = { MultipartException.class })
	public ResponseEntity<Object> handleApiRequestException(MultipartException ex) {

		HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);
	}
	
	// handle: when the current request gave a parameter that does not satisfy the EmailMsgIn object
	@ExceptionHandler(value = { EmailServerException.class })
	public ResponseEntity<Object> handleApiRequestException(EmailServerException ex) {

		HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);
	}
	
	// handle: invalid or expired JWT bearer token given
	@ExceptionHandler(value = { JwtException.class , MalformedJwtException.class})
	public ResponseEntity<Object> handleApiRequestJwtException(RuntimeException ex) {

		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);
	}
	
	
	// handle: invalid email or password
	@ExceptionHandler(value = { InvalidCredentialsException.class , UsernameNotFoundException.class})
	public ResponseEntity<Object> handleApiRequestCredentialException(RuntimeException ex) {

		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

		ApiExceptionModel apiException = new ApiExceptionModel(ex.getMessage(), httpStatus,
				ZonedDateTime.now(ZoneId.of("Z")));

		return new ResponseEntity<>(apiException, httpStatus);
	}

	
	

}
