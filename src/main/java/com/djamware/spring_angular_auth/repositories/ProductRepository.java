package com.djamware.spring_angular_auth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import com.djamware.spring_angular_auth.models.Products;

public interface ProductRepository extends MongoRepository<Products, String> {

    @Override
    void delete(@NonNull Products deleted);
}