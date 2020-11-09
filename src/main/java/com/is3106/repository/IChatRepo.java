package com.is3106.repository;

import org.springframework.data.repository.CrudRepository;

import com.is3106.entity.Chat;

public interface IChatRepo extends CrudRepository<Chat, String> {

}
