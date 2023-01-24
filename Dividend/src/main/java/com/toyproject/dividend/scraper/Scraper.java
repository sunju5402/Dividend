package com.toyproject.dividend.scraper;

import com.toyproject.dividend.model.Company;
import com.toyproject.dividend.model.ScrapedResult;

public interface Scraper {
	Company scrapCompanyByTicker(String ticker);
	ScrapedResult scrap(Company company);
}
