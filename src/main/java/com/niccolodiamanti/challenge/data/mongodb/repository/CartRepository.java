package com.niccolodiamanti.challenge.data.mongodb.repository;

import com.niccolodiamanti.challenge.data.mongodb.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {

}
