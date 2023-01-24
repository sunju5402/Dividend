package com.toyproject.dividend.web;

import com.toyproject.dividend.model.Company;
import com.toyproject.dividend.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

	private final CompanyService companyService;

	@GetMapping("/autocomplete")
	public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
		return ResponseEntity.ok(companyService.getCompanyNamesByKeyword(keyword));
	}

	@GetMapping("")
	public ResponseEntity<?> searchCompany(final Pageable pageable) {
		return ResponseEntity.ok(companyService.getAllCompany(pageable));
	}

	@PostMapping("")
	public ResponseEntity<?> addCompany(@RequestBody Company request) {
		String ticker = request.getTicker().trim();
		if (ObjectUtils.isEmpty(ticker)) {
			throw new RuntimeException("ticker is empty");
		}

		Company company = companyService.save(ticker);
		companyService.addAutocompleteKeyword(company.getName());

		return ResponseEntity.ok(company);
	}

	@DeleteMapping("")
	public ResponseEntity<?> deleteCompany() {
		return null;
	}
}
