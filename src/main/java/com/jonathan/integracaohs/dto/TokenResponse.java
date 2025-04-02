package com.jonathan.integracaohs.dto;

import lombok.Data;

@Data
public class TokenResponse {

	private String token_type;
	private String refresh_token;
	private String access_token;
	private String expires_in;
	
}
