package com.toyproject.dividend.scheduler;

import static com.toyproject.dividend.model.constants.CacheKey.KEY_FINANCE;

import com.toyproject.dividend.model.Company;
import com.toyproject.dividend.model.ScrapedResult;
import com.toyproject.dividend.persist.CompanyRepository;
import com.toyproject.dividend.persist.DividendRepository;
import com.toyproject.dividend.persist.entity.CompanyEntity;
import com.toyproject.dividend.persist.entity.DividendEntity;
import com.toyproject.dividend.scraper.YahooFinanceScraper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;
	private final YahooFinanceScraper yahooFinanceScraper;

	@CacheEvict(value = KEY_FINANCE, allEntries = true)
	@Scheduled(cron = "${scheduler.scrap.yahoo}")
	// TODO 배치 기능
	public void yahooFinanceScheduling() {
		// 저장된 회사 목록 조회
		List<CompanyEntity> companies = companyRepository.findAll();

		// 회사마다 배당금 정보를 새로 스크래핑
		for (CompanyEntity company : companies) {
			log.info("scraping scheduler is started -> " + company.getName());
			ScrapedResult scrapedResult = yahooFinanceScraper.scrap(
				new Company(company.getName(), company.getTicker()));

			// 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
			scrapedResult.getDividends().stream()
				// model -> entity mapping
				.map(e -> new DividendEntity(company.getId(), e))
				.forEach(e -> {
					boolean exists = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
					if (!exists) {
						dividendRepository.save(e);
						log.info("insert new dividend -> " + e.toString());
					}
				});

			// 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
			// for문 안에 써줘야 함.
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

	}
}
