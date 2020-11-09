package com.is3106.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.is3106.common.TimeHelper;

import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
@NamedQuery(name = "Transaction.findAll", query = "SELECT u FROM Transaction u")
public @Data class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	private String id;

	@Column(name = "create_time")
	private Timestamp createTime;

	private BigDecimal amount;
	
	private String lat;
	
	private String lon;
	
	private String location;
	
	private String status;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private User sender;
	
	public Transaction() {
		this.createTime = TimeHelper.getCurrentTimestamp();
	}

}