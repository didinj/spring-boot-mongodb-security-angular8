package com.djamware.spring_angular_auth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.djamware.spring_angular_auth.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}