package com.is3106.dto.out;

import java.util.Date;

import lombok.Data;

public @Data class ChatOut {

	public String firstName;
	public String lastName;
	
	public String message;
	public Date createTime;
}
