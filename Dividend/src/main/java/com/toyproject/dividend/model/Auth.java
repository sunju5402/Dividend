package com.toyproject.dividend.model;

import com.toyproject.dividend.persist.entity.MemberEntity;
import java.util.List;
import lombok.Data;

public class Auth {
	@Data
	public static class SignIn {
		private String username;
		private String password;
	}

	@Data
	public static class SingUp {
		private String username;
		private String password;
		private List<String> roles;

		public MemberEntity toEntity() {
			return MemberEntity.builder()
								.username(this.username)
								.password(this.password)
								.roles(this.roles)
								.build();
		}
	}
}
