package com.junction2022.common.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import fi.vtt.utils.text.TextUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
public class UserProperties {

	private String name;
	private String password;
	
	private String basicAuthorization;

	@Getter
	private String[] roles;

	public String getName() {
		return name != null ? name : getDecodedAuthorizationString(true);
	}

	public String getPassword() {
		return password != null ? password : getDecodedAuthorizationString(false);
	}

	private String getDecodedAuthorizationString(final boolean beforeColon) {
		if (basicAuthorization != null) {
			final byte[] basicAuthorizationBytes = basicAuthorization.trim().getBytes(StandardCharsets.UTF_8);
			final String decodedAuthorization = new String(Base64.getDecoder().decode(basicAuthorizationBytes), StandardCharsets.UTF_8);
			final int index = decodedAuthorization.indexOf(TextUtils.COLON_CHAR);
			if (index < 0) {
				throw new IllegalArgumentException("Invalid authorization string");
			}
			return beforeColon ? decodedAuthorization.substring(0, index) : decodedAuthorization.substring(index + 1);
		}

		return null;
	}
}