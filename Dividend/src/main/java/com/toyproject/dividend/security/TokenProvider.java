package com.toyproject.dividend.security;

import com.toyproject.dividend.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

	private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1hour
	private static final String KEY_ROLES = "roles";
	private final MemberService memberService;

	@Value("${spring.jwt.secret}")
	private String secretKey;


	/**
	 * 토큰 생성(발급)
	 */
	public String generateToken(String username, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put(KEY_ROLES, roles);

		Date now = new Date();
		Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now) // 토큰 생성 시간
			.setExpiration(expiredDate) // 토큰 만료 시간
			.signWith(SignatureAlgorithm.HS512, secretKey) // 사용할 암호화 알고리즘, 비밀키
			.compact();
	}

	public Authentication getAuthentication(String jwt) {
		UserDetails userDetails = memberService.loadUserByUsername(getUsername(jwt));
		log.info("username -> " + getUsername(jwt));
		log.info(("getAuthorities --------> ") + userDetails.getAuthorities().size());
		return new UsernamePasswordAuthenticationToken(userDetails, "",
			userDetails.getAuthorities());
	}

	public String getUsername(String token) {
		return parseClaims(token).getSubject(); // id 획득
	}

	public boolean isValidToken(String token) {
		if (!StringUtils.hasText(token)) return false;

		Claims claims = parseClaims(token);
		return !claims.getExpiration().before(new Date()); // 토큰의 만료시간이 지금 시간의 이전인지
	}

	private Claims parseClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
