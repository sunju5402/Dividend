package com.toyproject.dividend.service;

import com.toyproject.dividend.model.Company;
import com.toyproject.dividend.model.ScrapedResult;
import com.toyproject.dividend.persist.CompanyRepository;
import com.toyproject.dividend.persist.DividendRepository;
import com.toyproject.dividend.persist.entity.CompanyEntity;
import com.toyproject.dividend.persist.entity.DividendEntity;
import com.toyproject.dividend.scraper.Scraper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
public class CompanyService {

	private final Scraper yahooFinanceScraper;

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	public Company save(String ticker) {
		boolean exists = companyRepository.existsByTicker(ticker);
		if (exists) {
			throw new RuntimeException("already exists ticker -> " + ticker);
		}
		return storeCompanyAndDividend(ticker);
	}

	public Page<CompanyEntity> getAllCompany(Pageable pageable) {
		return companyRepository.findAll(pageable);
	}

	private Company storeCompanyAndDividend(String ticker) {
		// ticker 를 기준으로 회사를 스크래핑
		Company company = yahooFinanceScraper.scrapCompanyByTicker(ticker);

		if (ObjectUtils.isEmpty(company)) {
			throw new RuntimeException("failed to scrap ticker -> " + ticker);
		}

		// 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
		ScrapedResult scrapedResult = yahooFinanceScraper.scrap(company);

		// 스크래핑 결과
		CompanyEntity companyEntity = companyRepository.save(new CompanyEntity(company));
		List<DividendEntity> dividendEntities = scrapedResult.getDividends().stream()
											.map(e -> new DividendEntity(companyEntity.getId(), e))
											.collect(Collectors.toList());
		dividendRepository.saveAll(dividendEntities);
		return company;
	}
}
