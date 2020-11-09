package com.is3106.dto.out;

public class JwtResponseOut {
	
	private final String jwt;
	
	public String userId;

	public JwtResponseOut(String jwt, String userId) {
		super();
		this.jwt = jwt;
		this.userId = userId;
	}

	public String getJwt() {
		return jwt;
	}

}
