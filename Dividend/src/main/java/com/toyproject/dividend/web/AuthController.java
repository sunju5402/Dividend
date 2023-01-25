package com.toyproject.dividend.web;

import com.toyproject.dividend.model.Auth.SignIn;
import com.toyproject.dividend.model.Auth.SignUp;
import com.toyproject.dividend.persist.entity.MemberEntity;
import com.toyproject.dividend.security.TokenProvider;
import com.toyproject.dividend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;

	private final TokenProvider tokenProvider;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUp request) {
		return ResponseEntity.ok(memberService.register(request));
	}

	@PostMapping("/signin")
	public ResponseEntity<?> signin(@RequestBody SignIn request) {
		MemberEntity member = memberService.authenticate(request);
		String token = tokenProvider.generateToken(member.getUsername(), member.getRoles());
		log.info("user login -> " + request.getUsername());
		return ResponseEntity.ok(token);
	}
}
