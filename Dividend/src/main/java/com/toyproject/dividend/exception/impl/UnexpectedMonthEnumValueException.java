package com.toyproject.dividend.exception.impl;

import com.toyproject.dividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UnexpectedMonthEnumValueException extends AbstractException {

	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}

	@Override
	public String getMessage() {
		return "기대하지 않은 Month enum value 입니다.";
	}
}
