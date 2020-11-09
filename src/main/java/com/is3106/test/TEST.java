package com.is3106.test;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.is3106.dto.BaseResponseDto;


@Controller
@RequestMapping(path = "/test")
public class TEST {
	
    @Autowired
    private JavaMailSender javaMailSender;
	
	@Transactional
	@GetMapping("/tt")
	public ResponseEntity<BaseResponseDto<?>> tt(){
		sendEmail("lesteryap16@gmail.com", "hello", "testt");
		return BaseResponseDto.success("send!");
	}
	
    void sendEmail(String sendTo, String subject, String msg) {

        SimpleMailMessage mmsg = new SimpleMailMessage();
        mmsg.setTo(sendTo);

        mmsg.setSubject(subject);
        mmsg.setText(msg);
        
        javaMailSender.send(mmsg);

    }


}
