package com.toyproject.dividend.exception.impl;

import com.toyproject.dividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class FailToScrapTickerException extends AbstractException {

	@Override
	public int getStatusCode() {
		return HttpStatus.BAD_REQUEST.value();
	}

	@Override
	public String getMessage() {
		return "해당 ticker로 스크래핑에 실패하였습니다.";
	}
}
