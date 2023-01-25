package com.toyproject.dividend.service;

import com.toyproject.dividend.model.Company;
import com.toyproject.dividend.model.Dividend;
import com.toyproject.dividend.model.ScrapedResult;
import com.toyproject.dividend.persist.CompanyRepository;
import com.toyproject.dividend.persist.DividendRepository;
import com.toyproject.dividend.persist.entity.CompanyEntity;
import com.toyproject.dividend.persist.entity.DividendEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	// 요청이 자주 들어오고 자주 변경되지 않는 데이터이기에 reids 적용
	@Cacheable(key = "{#companyName}", value = "finance")
	public ScrapedResult getDividendByCompanyName(String companyName) {
		log.info("redis test - search company -> " + companyName);

		// 회사명을 기준으로 회사 정보를 조회
		CompanyEntity company = companyRepository.findByName(companyName)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다."));

		// 조회된 회사 ID로 배당금 정보 조히
		List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(
			company.getId());

		// 결과 조합 후 반환
		List<Dividend> dividends = dividendEntities.stream()
													.map(e -> new Dividend(e.getDate(),
														e.getDividend()))
													.collect(Collectors.toList());

		return new ScrapedResult(
			new Company(company.getTicker(), company.getName()), dividends);
	}
}
