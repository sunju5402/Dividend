package com.toyproject.dividend.service;

import com.toyproject.dividend.model.Company;
import com.toyproject.dividend.model.Dividend;
import com.toyproject.dividend.model.ScrapedResult;
import com.toyproject.dividend.persist.CompanyRepository;
import com.toyproject.dividend.persist.DividendRepository;
import com.toyproject.dividend.persist.entity.CompanyEntity;
import com.toyproject.dividend.persist.entity.DividendEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FinanceService {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	public ScrapedResult getDividendByCompanyName(String companyName) {

		// 회사명을 기준으로 회사 정보를 조회
		CompanyEntity company = companyRepository.findByName(companyName)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다."));

		// 조회된 회사 ID로 배당금 정보 조히
		List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(
			company.getId());

		// 결과 조합 후 반환
		List<Dividend> dividends = dividendEntities.stream()
													.map(e -> Dividend.builder()
														.date(e.getDate())
														.dividend(e.getDividend())
														.build())
													.collect(Collectors.toList());

		return new ScrapedResult(Company.builder()
										.ticker(company.getTicker())
										.name(company.getName())
										.build(),
			dividends);
	}
}
