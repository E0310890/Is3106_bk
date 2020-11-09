package com.is3106.jwt;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.is3106.entity.User;
import com.is3106.repository.IUserRepo;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private IUserRepo userRepo;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email);
		List<SimpleGrantedAuthority> grantedAuthorities = new LinkedList<>();
		if (user == null)
			throw new UsernameNotFoundException("User not found with email: " + email);

//		user.getUserVsRoles().forEach(r -> {
//			SimpleGrantedAuthority role = new SimpleGrantedAuthority(r.getRole().getName());
//			grantedAuthorities.add(role);
//			System.out.println("--roles: ");
//			System.out.println(r.getRole().getName());
//		});
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				grantedAuthorities);

	}

}