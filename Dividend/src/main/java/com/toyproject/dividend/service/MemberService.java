package com.toyproject.dividend.service;

import com.toyproject.dividend.model.Auth;
import com.toyproject.dividend.persist.MemberRepository;
import com.toyproject.dividend.persist.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("couldn't find user" + username));
	}

	public MemberEntity register(Auth.SingUp member) {
		boolean exists = memberRepository.existsByUsername(member.getUsername());
		if (exists) {
			throw new RuntimeException("이미 사용 중인 아이디 입니다.");
		}

		member.setPassword(passwordEncoder.encode(member.getPassword()));

		return memberRepository.save(member.toEntity());
	}

	public MemberEntity authenticate() {
		return null;
	}
}
