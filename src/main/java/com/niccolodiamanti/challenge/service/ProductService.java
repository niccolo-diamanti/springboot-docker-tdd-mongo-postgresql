package com.niccolodiamanti.challenge.service;

import com.niccolodiamanti.challenge.data.postgre.model.Product;

import java.util.Optional;

public interface ProductService {

    Optional<Product> get(String id);
}
