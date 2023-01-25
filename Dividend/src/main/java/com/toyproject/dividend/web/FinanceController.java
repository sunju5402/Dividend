package com.toyproject.dividend.web;

import com.toyproject.dividend.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class FinanceController {

	private final FinanceService financeService;

	@GetMapping("/dividend/{companyName}")
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> searchFinance(@PathVariable String companyName) {
		return ResponseEntity.ok(financeService.getDividendByCompanyName(companyName));
	}
}
