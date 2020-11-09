package com.is3106.dto.out;

import java.math.BigDecimal;
import java.util.Date;


import com.is3106.entity.User;

import lombok.Data;

public @Data class UserOut {
	public String lat;
    public String lon;
    public String email;
    public String lastName;
    public String firstName;
    public String password;
    public BigDecimal walletBalance;
    
	public String cardName;
	public String cardNumber;
	public Integer cvv;
	public Date cardExpiryDate;
    
    //ping
    public boolean timesUp;
    public boolean successfullyFound;
    public BigDecimal pingAmt;
    public String distanceAway;
    
    public static UserOut converUser(User u) {
    	UserOut uo = new UserOut();
    	uo.lat = u.getLat();
    	uo.lon = u.getLon();
    	uo.email = u.getEmail();
    	uo.lastName = u.getLastName();
    	uo.firstName = u.getFirstName();
    	uo.walletBalance = u.getWalletBalance();
    	
    	uo.cardName = u.getCardName();
    	uo.cardNumber = u.getCardName();
    	uo.cvv = u.getCvv();
    	uo.cardExpiryDate = u.getCardExpiryDate();
    	return uo;
    }
}
