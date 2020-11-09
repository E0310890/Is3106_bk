package com.is3106.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.is3106.common.MapperHelper;
import com.is3106.dto.BaseResponseDto;
import com.is3106.dto.in.UserIn;
import com.is3106.dto.out.JwtResponseOut;
import com.is3106.dto.out.UserOut;
import com.is3106.entity.User;
import com.is3106.repository.IUserRepo;


@Controller
@RequestMapping(path = "/account")
public class AccountController {

	@Autowired
	private com.is3106.jwt.JwtUserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private com.is3106.jwt.JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private IUserRepo userRepo;


	@Transactional
	@PostMapping("/signUp")
	public @ResponseBody ResponseEntity<BaseResponseDto<?>> signUp(@RequestBody UserIn userIn) {
		//valid
		if (userIn.password == null || userIn.firstName == null || userIn.email == null)
			return BaseResponseDto.fail("email, first_name or password cannot be null");
		
		if (userRepo.findByEmail(userIn.email) != null)
			return BaseResponseDto.fail("email choosen already existed..");
		if(!userIn.password.equals(userIn.confirmPassword)) 
			return BaseResponseDto.fail("password and confirm password does not match..");
		User u = new User();
		u.setEmail(userIn.email);
		u.setPassword(bcryptEncoder.encode(userIn.password));
		u.setFirstName(userIn.firstName);
		u.setLastName(userIn.lastName);
		userRepo.save(u);
		return BaseResponseDto.success("Successfully sign up!");
	}

	
	@Transactional
	@PostMapping("/login")
	public @ResponseBody ResponseEntity<BaseResponseDto<JwtResponseOut>> login(@RequestBody UserIn userIn)
			throws Exception {
		authenticate(userIn.getEmail(), userIn.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userIn.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);

		String userId = userRepo.findByEmail(jwtTokenUtil.getUsernameFromToken(token)).getId();
		return BaseResponseDto.success("successfully logged in", new JwtResponseOut(token, userId));
	}
	
	//get user detail
	@Transactional
	@GetMapping("/userData")
	public @ResponseBody ResponseEntity<BaseResponseDto<UserOut>> userData() {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);
		
		UserOut uo = UserOut.converUser(u);
		
		return BaseResponseDto.success("success", uo);
		
	}
	
	@Transactional
	@PostMapping("/updProfile")
	public @ResponseBody ResponseEntity<BaseResponseDto<?>> updProfile(@RequestBody UserIn userIn) {
		String email = jwtTokenUtil.getUserEmail();
		User u = userRepo.findByEmail(email);
		User newU = MapperHelper.mergeObjects(userIn, u);
		if(userIn.password != null)
			newU.setPassword(bcryptEncoder.encode(userIn.password));
		userRepo.save(newU);
		return BaseResponseDto.success("success");
		
	}


	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", e);
		}
	}

}
