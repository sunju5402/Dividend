package com.toyproject.dividend.service;

import com.toyproject.dividend.exception.impl.AlreadyExistCompanyException;
import com.toyproject.dividend.exception.impl.FailToScrapTickerException;
import com.toyproject.dividend.exception.impl.NoCompanyException;
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
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
public class CompanyService {

	private final Trie trie;
	private final Scraper yahooFinanceScraper;

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	public Company save(String ticker) {
		boolean exists = companyRepository.existsByTicker(ticker);
		if (exists) {
			throw new AlreadyExistCompanyException();
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
			throw new FailToScrapTickerException();
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

	// 메모리 더 안 써도 되는 장점, db 부하 단점
	public List<String> getCompanyNamesByKeyword(String keyword) {
		Pageable limit = PageRequest.of(0, 10);
		Page<CompanyEntity> companyEntities = companyRepository.findByNameStartingWithIgnoreCase(
			keyword, limit);
		return companyEntities.stream()
			.map(e -> e.getName())
			.collect(Collectors.toList());
	}

	public void addAutocompleteKeyword(String keyword) {
		trie.put(keyword, null);
	}

	public List<String> autocomplete(String keyword) {
		return (List<String>) trie.prefixMap(keyword).keySet()
			.stream().limit(10).collect(Collectors.toList());
	}

	public void deleteAutocompleteKeyword(String keyword) {
		trie.remove(keyword);
	}

	public String deleteCompany(String ticker) {
		CompanyEntity company = companyRepository.findByTicker(ticker)
			.orElseThrow(() -> new NoCompanyException());

		dividendRepository.deleteAllByCompanyId(company.getId());
		companyRepository.delete(company);

		deleteAutocompleteKeyword(company.getName());

		return company.getName();
	}
}
