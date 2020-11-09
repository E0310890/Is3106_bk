package com.is3106.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.is3106.common.TimeHelper;
import com.is3106.dto.BaseResponseDto;
import com.is3106.dto.in.UserIn;
import com.is3106.dto.out.ChatOut;
import com.is3106.dto.out.TransactionOut;
import com.is3106.dto.out.UserOut;
import com.is3106.entity.Chat;
import com.is3106.entity.Transaction;
import com.is3106.entity.User;
import com.is3106.repository.IChatRepo;
import com.is3106.repository.ITransactionRepo;
import com.is3106.repository.IUserRepo;

@Controller
@RequestMapping(path = "/main")
public class MainController {

	@Autowired
	private com.is3106.jwt.JwtTokenUtil jwtTokenUtil;

	@Autowired
	private IUserRepo userRepo;

	@Autowired
	private ITransactionRepo transactionRepo;
	
	@Autowired
	private IChatRepo chatRepo;

	final long tresholdMin = 5;

	// update user pos (lat, lon)jwt
	@Transactional
	@GetMapping("/updUserPos")
	public @ResponseBody ResponseEntity<BaseResponseDto<?>> updUserPos(@RequestParam String lat,
			@RequestParam String lon) {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);
		u.setLat(lat);
		u.setLon(lon);
		userRepo.save(u);
		return BaseResponseDto.success("success");
	}

	// get all user pos ();
	@Transactional
	@GetMapping("/getAllUserPos")
	public @ResponseBody ResponseEntity<BaseResponseDto<List<UserOut>>> getAllUserPos() {
		List<UserOut> userList = new LinkedList<>();
		userRepo.findAll().forEach(x -> {
			userList.add(UserOut.converUser(x));
		});
		return BaseResponseDto.success("success", userList);
	}

	// ping (jwt, amt) validate nearest ppl near by..then accept
	@Transactional
	@GetMapping("/ping")
	public @ResponseBody ResponseEntity<BaseResponseDto<String>> ping(@RequestParam BigDecimal amt,
			@RequestParam String location) {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);

		// validate enough cash in wallet (wallet cash >= ping amt)
		if (u.getWalletBalance().compareTo(amt) == -1)
			return BaseResponseDto.fail("not enough cash in wallet (wallet cash >= ping amt)", null);

		Transaction t = new Transaction();
		t.setAmount(amt);
		t.setStatus("PENDING");
		t.setLocation(location);
		t.setUser(u);

		Transaction newT = transactionRepo.save(t);

		return BaseResponseDto.success("success", newT.getId());
	}

	// pingCaller
	// timesUp == true -> no ppl close by found
	// successfullyFound == false -> not found yet
	// if timesUp == false and successfullyFound == true -> successfully found ppl
	@Transactional
	@GetMapping("/pingCall")
	public @ResponseBody ResponseEntity<BaseResponseDto<UserOut>> pingCall(@RequestParam String transactionId) {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);
		UserOut uo = new UserOut();

		Optional<Transaction> ot = transactionRepo.findById(transactionId);
		if (ot.isPresent()) {
			// 5min treshold
			Transaction t = ot.get();
			// check if within 5min of created time.
			long diff = TimeHelper.getCurrentTimestamp().getTime() - t.getCreateTime().getTime();
			System.out.println(TimeHelper.getCurrentTimestamp().getTime());
			System.out.println((diff / 1000) / 60 + "min");
			if ((diff / 1000) / 60 > tresholdMin) {
				uo.timesUp = true;
				t.setStatus("TIMESUP");
				transactionRepo.save(t);
			} else {
				// check transact ststus is pending or agreed
				if (t.getStatus() == "PENDING")
					uo.successfullyFound = false;
				else if (t.getStatus() == "AGREED") {
					// if agrred, send user data as well as distance.
					User x = t.getSender();
					uo = UserOut.converUser(x);
					uo.successfullyFound = true;
					double d = distance(Double.parseDouble(u.getLat()), Double.parseDouble(u.getLon()),
							Double.parseDouble(x.getLat()), Double.parseDouble(x.getLon()));
					uo.distanceAway = String.valueOf(d) + " km";
					uo.pingAmt = t.getAmount();
				}

			}

		} else {
			return BaseResponseDto.fail("transaction id provided does not exist..", null);
		}

		return BaseResponseDto.success("success", uo);
	}

	@Transactional
	@GetMapping("/notificationCall")
	public @ResponseBody ResponseEntity<BaseResponseDto<List<TransactionOut>>> notificationCall() {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);

		List<TransactionOut> data = new LinkedList<>();

		List<Transaction> dataList = transactionRepo.findAllByStatus("PENDING");
		for (Transaction t : dataList) {
			long diff = TimeHelper.getCurrentTimestamp().getTime() - t.getCreateTime().getTime();
			if ((diff / 1000) / 60 > tresholdMin) {
				// timesup
			} else {
				User x = t.getUser();
				TransactionOut to = new TransactionOut();
				double d = distance(Double.parseDouble(u.getLat()), Double.parseDouble(u.getLon()),
						Double.parseDouble(x.getLat()), Double.parseDouble(x.getLon()));
				to.setRcvId(x.getId());
				to.setDistanceAway(d + "km");
				to.setFirstName(x.getFirstName());
				to.setLastName(x.getLastName());
				to.setPingAmt(t.getAmount());
				to.setTransactionId(t.getId());
				data.add(to);
			}
		}

		return BaseResponseDto.success("success", data);
	}

	@Transactional
	@GetMapping("/notificationAccept")
	public @ResponseBody ResponseEntity<BaseResponseDto<?>> notificationAccept(@RequestParam String transactionId) {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);

		Optional<Transaction> ot = transactionRepo.findById(transactionId);
		if (ot.isPresent()) {
			Transaction t = ot.get();
			long diff = TimeHelper.getCurrentTimestamp().getTime() - t.getCreateTime().getTime();
			if ((diff / 1000) / 60 > tresholdMin) {
				return BaseResponseDto.fail("transaction time is already up");
			}
			t.setSender(u);
			t.setStatus("APPROVED");
			transactionRepo.save(t);
		} else {
			return BaseResponseDto.fail("transaction id provided does not exist..");
		}

		return BaseResponseDto.success("success");
	}

	// transfer cash
	@Transactional
	@GetMapping("/pingScan")
	public @ResponseBody ResponseEntity<BaseResponseDto<?>> pingScan(@RequestParam String transactionId) {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);

		Optional<Transaction> ot = transactionRepo.findById(transactionId);
		if (ot.isPresent()) {
			Transaction t = ot.get();
			User rcv = t.getUser();
			User snd = t.getSender();
			BigDecimal amt = t.getAmount();

			// rcv(cash) will give snd wallet money.
			rcv.setWalletBalance(rcv.getWalletBalance().subtract(amt));
			snd.setWalletBalance(snd.getWalletBalance().add(amt));

			userRepo.save(rcv);
			userRepo.save(snd);

			t.setStatus("COMPLETED");
			transactionRepo.save(t);

		} else {
			return BaseResponseDto.fail("transaction id provided does not exist..");
		}

		return BaseResponseDto.success("success");
	}

	@Transactional
	@GetMapping("/topUpWallet")
	public @ResponseBody ResponseEntity<BaseResponseDto<?>> topUpWallet(@RequestParam BigDecimal topUpamt) {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);

		u.setWalletBalance(u.getWalletBalance().add(topUpamt));
		userRepo.save(u);
		return BaseResponseDto.success("success");
	}

	// getall prev transactions
	@Transactional
	@GetMapping("/prevTransactions")
	public @ResponseBody ResponseEntity<BaseResponseDto<List<TransactionOut>>> prevTransactions() {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);

		List<TransactionOut> data = new LinkedList<>();
		// negate
		for (Transaction t : u.getTransactions()) {
			TransactionOut to = new TransactionOut();
			User sender = t.getSender();
			to.setFirstName(sender.getFirstName());
			to.setLastName(sender.getLastName());
			to.setPingAmt(t.getAmount().negate());
			to.setCreateTime(t.getCreateTime());
			to.setTransactionId(t.getId());
			to.setLocation(t.getLocation());
			data.add(to);
		}
		// +ve
		for (Transaction t : u.getSenderTransactions()) {
			TransactionOut to = new TransactionOut();
			User sender = t.getSender();
			to.setFirstName(sender.getFirstName());
			to.setLastName(sender.getLastName());
			to.setPingAmt(t.getAmount().negate());
			to.setCreateTime(t.getCreateTime());
			to.setTransactionId(t.getId());
			to.setLocation(t.getLocation());
			data.add(to);
		}

		Comparator<TransactionOut> cmpr = (TransactionOut o1, TransactionOut o2) -> o1.getCreateTime()
				.compareTo(o2.getCreateTime());
		Collections.sort(data, cmpr);
		return BaseResponseDto.success("success", data);
	}
	
	//chat 
//	@Transactional
//	@GetMapping("/chatCaller")
//	public @ResponseBody ResponseEntity<BaseResponseDto<List<TransactionOut>>> chatCaller() {
//		
//	}
	
	//chat 
	@Transactional
	@GetMapping("/chatSend")
	public @ResponseBody ResponseEntity<BaseResponseDto<?>> chatSend(@RequestParam String message, @RequestParam String UserId) {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);
		
		Optional<User> osendTo = userRepo.findById(UserId);
		if(osendTo.isPresent()) {
			User sendTo = osendTo.get();
			Chat c = new Chat();
			c.setUser(u);
			c.setReceiver(sendTo);
			c.setMessage(message);
			chatRepo.save(c);
		}else {
			return BaseResponseDto.fail("user id provided does not exist..");
		}
		return BaseResponseDto.success("success");
	}

//	---

	private static double distance(double lat1, double lon1, double lat2, double lon2) {
		String unit = "K";
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		} else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
					+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			if (unit.equals("K")) {
				dist = dist * 1.609344;
			} else if (unit.equals("N")) {
				dist = dist * 0.8684;
			}
			return (dist);
		}
	}
}

////ping (jwt, amt) validate nearest ppl near by..then accept
//@Transactional
//@GetMapping("/ping")
//public @ResponseBody ResponseEntity<BaseResponseDto<UserOut>> ping(@RequestParam BigDecimal amt) {
//	String email = jwtTokenUtil.getUserEmail();
//	User u = userRepo.findByEmail(email);
//	
//	double minDistance = Long.MAX_VALUE;
//	User minUser = null;
//	
//	List<User> userList = StreamSupport
//			  .stream(userRepo.findAll().spliterator(), false)
//			  .collect(Collectors.toList());
//	for(User x: userList) {
//		if(x == u || x.getWalletBalance().compareTo(amt) == -1)
//			continue;
//		double d = distance(Double.parseDouble(u.getLat()), Double.parseDouble(u.getLon()), Double.parseDouble(x.getLat()), Double.parseDouble(x.getLon()));
//		if(d<minDistance) {
//			minDistance = d;
//			minUser = x;
//		}
//	};
//	
//	if(minUser == null)
//		return BaseResponseDto.fail("no near by user that have the right amt..", null);
//	
//	UserOut uo = UserOut.converUser(minUser);
//	uo.distanceAway = String.valueOf(minDistance) + " km";
//	uo.pingAmt = amt;
//	
//
//	return BaseResponseDto.success("success, waiting for people to accept", uo);
//}