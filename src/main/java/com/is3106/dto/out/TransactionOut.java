package com.is3106.dto.out;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

public @Data class TransactionOut {

	public String transactionId;
    public BigDecimal pingAmt;
    public String distanceAway;
    public String firstName;
    public String lastName;
    
    private Date createTime;
    
    public String location;
    //for qr
    public String rcvId;
	
}
