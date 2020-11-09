package com.is3106;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.is3106.entity.User;
import com.is3106.repository.IUserRepo;

@Component
@Order(1)
class MyCommandLineRunner implements CommandLineRunner {

	@Autowired
	private IUserRepo userRepo;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public void run(String... args) throws Exception {
		if (args.length > 0) {
			System.out.print("ajknasjknasjknska");
		}
//		1.2954468, 103.7752822
//		1.294958, 103.774563
//		1.295245, 103.772919
//		1.295701, 103.774358
//		1.294684, 103.772638

		if (userRepo.findByEmail("emailxx1@gmail.com") == null) {

			User u1 = new User("1.2954468", "103.7752822", "emailxx1@gmail.com", "user1",
					"user1", new BigDecimal(100), bcryptEncoder.encode("123456"));
			User u2 = new User("1.294958", "103.774563", "emailxx2@gmail.com", "user2",
					"user2", new BigDecimal(100), bcryptEncoder.encode("123456"));
			User u3 = new User("1.295245", "103.772919", "emailxx3@gmail.com", "user3",
					"user3", new BigDecimal(100), bcryptEncoder.encode("123456"));
			User u4 = new User("1.295701", "103.774358", "emailxx4@gmail.com", "user4",
					"user4", new BigDecimal(100), bcryptEncoder.encode("123456"));
			User u5 = new User("1.294684", "103.772638", "emailxx5@gmail.com", "user5",
					"user5", new BigDecimal(100), bcryptEncoder.encode("123456"));

			userRepo.save(u1);
			userRepo.save(u2);
			userRepo.save(u3);
			userRepo.save(u4);
			userRepo.save(u5);
		}

	}

}