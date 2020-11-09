package com.is3106.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.is3106.common.TimeHelper;

import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "chat")
@NamedQuery(name = "Chat.findAll", query = "SELECT u FROM Chat u")
public @Data class Chat implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	private String id;

	@Column(name = "create_time")
	private Timestamp createTime;

	private String message;
	
	private User receiver;
	
	@ManyToOne
	private User user;
	
	public Chat() {
		createTime = TimeHelper.getCurrentTimestamp();
	}

}