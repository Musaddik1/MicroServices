package com.bridgelabz.micro.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.micro.model.User;

import java.lang.String;
                                                                       
public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUserId(String userid);

	Optional<User> findByEmail(String email);

}
