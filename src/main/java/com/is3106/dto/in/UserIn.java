package com.is3106.dto.in;

import lombok.Data;

public @Data class UserIn {

	public String userId;
	
	public String firstName;
	
	public String lastName;
	
	public String email;
	
	public String password;
	
	public String confirmPassword;
}
