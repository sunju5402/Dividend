package com.toyproject.dividend.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 한 요청당 한번 필터가 실행
// filter -> servlet -> interceptor -> aop -> controller
	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer "; // 인증 타입을 나태내기 위해 사용 jwt는 Bearer

	private final TokenProvider tokenProvider;

	// 요청에 토큰이 들어있는지 확인 후, 토큰 유효성 검증
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = resolveTokenFromRequest(request);

		if (StringUtils.hasText(token) && tokenProvider.isValidToken(token)) {
			// 토큰 유효성 검증
			Authentication auth = tokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);

			log.info(String.format("[%s] -> %s", tokenProvider.getUsername(token),
				request.getRequestURI()));
		}

		filterChain.doFilter(request, response);
	}

	private String resolveTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader(TOKEN_HEADER);

		if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
			return token.substring(TOKEN_PREFIX.length()); // 실제 토큰에 해당하는 값을 반환
		}

		return null;
	}
}
