package com.djamware.spring_angular_auth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.djamware.spring_angular_auth.models.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
}