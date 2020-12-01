package com.niccolodiamanti.challenge.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niccolodiamanti.challenge.data.mongodb.model.Cart;
import com.niccolodiamanti.challenge.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCart_shouldReturnCartIfFound() throws Exception {

        String product1 = "ABC";
        String product2 = "XYZ";
        String product3 = "DEF";

        var expectedCart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .discount(50.50)
                .price(150.50)
                .products(new HashSet<>(Arrays.asList(product1, product2, product3)))
                .build();

        when(cartService.get(UUID.fromString(expectedCart.getId()))).thenReturn(Optional.of(expectedCart));

        mvc.perform(get("/cart/v1/" + expectedCart.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedCart.getId().toString())))
                .andExpect(jsonPath("$.discount", is(expectedCart.getDiscount())))
                .andExpect(jsonPath("$.price", is(expectedCart.getPrice())))
                .andExpect(jsonPath("$.products[0]", is(product1)))
                .andExpect(jsonPath("$.products[1]", is(product3)))
                .andExpect(jsonPath("$.products[2]", is(product2)));
    }

    @Test
    void getCart_shouldReturn404IfCartNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(cartService.get(id)).thenReturn(Optional.empty());

        mvc.perform(get("/cart/v1/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCart_shouldReturnCreatedCartIfOk() throws Exception {
        String product1 = "ABC";
        String product2 = "XYZ";
        String product3 = "DEF";

        var cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .discount(50.50)
                .price(150.50)
                .products(new HashSet<>(Arrays.asList(product1, product2, product3)))
                .build();

        when(cartService.create(cart)).thenReturn(cart);

        mvc.perform(
                post("/cart/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(cart))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(cart.getId().toString())))
                .andExpect(jsonPath("$.discount", is(cart.getDiscount())))
                .andExpect(jsonPath("$.price", is(cart.getPrice())))
                .andExpect(jsonPath("$.products[0]", is(product1)))
                .andExpect(jsonPath("$.products[1]", is(product3)))
                .andExpect(jsonPath("$.products[2]", is(product2)));
    }

    @Test
    void createCart_shouldReturnBadRequestIfCartIsInvalid() throws Exception {
        mvc.perform(
                post("/cart/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(null))
        )
                .andExpect(status().isBadRequest());
    }
}