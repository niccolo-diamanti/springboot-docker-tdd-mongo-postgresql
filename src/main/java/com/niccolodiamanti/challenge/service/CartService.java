package com.niccolodiamanti.challenge.service;

import com.niccolodiamanti.challenge.data.mongodb.model.Cart;
import com.niccolodiamanti.challenge.data.postgre.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface CartService {

    Optional<Cart> get(UUID id);

    Cart create(Cart cart);

    Cart update(UUID cartId, Product product);

    double calculateDiscount(Cart cart, Product lastAddedProduct);
}
