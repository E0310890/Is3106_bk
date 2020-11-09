package com.is3106.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.is3106.entity.Transaction;

public interface ITransactionRepo extends CrudRepository<Transaction, String>{
	
	List<Transaction> findAllByStatus(String status);

}
