package com.toyproject.dividend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Company {
	private String ticker;
	private String name;
}
