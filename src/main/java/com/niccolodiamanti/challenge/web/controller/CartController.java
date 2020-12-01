package com.niccolodiamanti.challenge.web.controller;

import com.niccolodiamanti.challenge.data.mongodb.model.Cart;
import com.niccolodiamanti.challenge.data.postgre.model.Product;
import com.niccolodiamanti.challenge.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/cart")
@Tag(name = "Cart")
public class CartController {

    private final CartService _cartService;

    public CartController(CartService cartService) {
        _cartService = cartService;
    }

    @PostMapping("/v1/create")
    ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        return ResponseEntity.ok(_cartService.create(cart));
    }

    @GetMapping("/v1/{cartId}")
    ResponseEntity<Cart> getCart(@PathVariable String cartId) {
        final Optional<Cart> cart = _cartService.get(UUID.fromString(cartId));
        if (cart.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cart.get());
    }

    @PutMapping("/v1/{cartId}")
    ResponseEntity<Cart> updateCart(@PathVariable UUID cartId, @RequestBody Product product) {
        final Cart cart = _cartService.update(cartId, product);
        return ResponseEntity.ok(cart);
    }
}
