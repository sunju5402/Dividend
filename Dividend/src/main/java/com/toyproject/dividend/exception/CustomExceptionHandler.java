package com.toyproject.dividend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice // controller보다 바깥쪽에 위치, filter보다는 controller에 가깝게 위치
public class CustomExceptionHandler {

	@ExceptionHandler(AbstractException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e) {
		ErrorResponse errorResponse = ErrorResponse.builder()
													.code(e.getStatusCode())
													.message(e.getMessage())
													.build();
		return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
	}
}
