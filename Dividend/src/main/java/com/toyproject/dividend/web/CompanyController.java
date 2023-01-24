package com.toyproject.dividend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {

	@GetMapping("/autocomplete")
	public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
		return null;
	}

	@GetMapping("")
	public ResponseEntity<?> searchCompany() {
		return null;
	}

	@PostMapping("")
	public ResponseEntity<?> addCompany() {
		return null;
	}

	@DeleteMapping("")
	public ResponseEntity<?> deleteCompany() {
		return null;
	}
}
