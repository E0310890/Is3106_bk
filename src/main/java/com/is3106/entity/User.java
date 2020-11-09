package com.is3106.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.is3106.common.TimeHelper;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name = "user")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public @Data class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	private String id;
	
	private String lat;
	
	private String lon;

	@Column(name = "create_time")
	private Timestamp createTime;

	@Column(unique = true)
	private String email;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "profilePicUrl")
	private String profilePicUrl;
	
	@Column(name = "wallet_balance")
	private BigDecimal walletBalance;

	private String password;
	
	private String role;
	
//	card details
	@Column(name = "card_name")
	private String cardName;
	
	@Column(name = "card_number")
	private String cardNumber;
	
	@Column(name = "card_expiry_date")
	private Date cardExpiryDate;
	
	private Integer cvv;
	
	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
	List<Transaction> senderTransactions;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Transaction> transactions;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Chat> chats;
	
	public User() {
		createTime = TimeHelper.getCurrentTimestamp();
	}

	public User(String lat, String lon, String email, String lastName, String firstName,
			BigDecimal walletBalance, String password) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.email = email;
		this.lastName = lastName;
		this.firstName = firstName;
		this.walletBalance = walletBalance;
		this.password = password;
		this.createTime = TimeHelper.getCurrentTimestamp();
	}
	
	

}