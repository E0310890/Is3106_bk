package com.is3106.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.is3106.entity.User;

@Repository
public interface IUserRepo extends CrudRepository<User, String>{
	
	User findByEmail(String email);

}
