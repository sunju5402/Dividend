package com.toyproject.dividend.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScrapedResult {

	private Company company;
	private List<Dividend> dividendEntities;

	public ScrapedResult() {
		this.dividendEntities = new ArrayList<>();
	}
}
