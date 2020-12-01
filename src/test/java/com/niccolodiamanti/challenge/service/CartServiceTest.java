package com.niccolodiamanti.challenge.service;

import com.niccolodiamanti.challenge.data.mongodb.model.Bundle;
import com.niccolodiamanti.challenge.data.mongodb.model.Cart;
import com.niccolodiamanti.challenge.data.mongodb.repository.BundleRepository;
import com.niccolodiamanti.challenge.data.mongodb.repository.CartRepository;
import com.niccolodiamanti.challenge.data.postgre.model.Product;
import com.niccolodiamanti.challenge.data.postgre.repository.ProductRepository;
import com.niccolodiamanti.challenge.service.impl.BundleServiceImpl;
import com.niccolodiamanti.challenge.service.impl.CartServiceImpl;
import com.niccolodiamanti.challenge.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BundleRepository bundleRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private BundleServiceImpl bundleService;

    @Mock
    private ProductServiceImpl productService;

    @Test
    void get_shouldReturnCart() {
        double discount = 0.3;
        double price = 150.50;
        String product1 = "FAWCD-2035";
        String product2 = "VYVLA-7385";

        var cart = Optional.of(
                Cart.builder()
                        .id(UUID.randomUUID().toString())
                        .products(Stream.of(product1, product2)
                                .collect(Collectors.toCollection(HashSet::new)))
                        .discount(discount)
                        .price(price)
                        .build());

        when(cartRepository.findById(cart.get().getId()))
                .thenReturn(cart);

        assertEquals(cart, cartService.get(UUID.fromString(cart.get().getId())));
    }

    @Test
    void get_shouldReturnEmptyIfNotFound() {
        var cartId = UUID.randomUUID();
        when(cartRepository.findById(cartId.toString()))
                .thenReturn(Optional.empty());

        assertTrue(cartService.get(cartId).isEmpty());
    }

    @Test
    void create_shouldCreateAndSaveTheCart() {
        double discount = 0.3;
        double price = 150.50;
        String product1 = "FAWCD-2035";
        String product2 = "VYVLA-7385";

        var cart =
                Cart.builder()
                        .products(Stream.of(product1, product2)
                                .collect(Collectors.toCollection(HashSet::new)))
                        .discount(discount)
                        .price(price)
                        .build();

        when(cartRepository.save(cart)).thenReturn(cart);
        assertEquals(cart, cartService.create(cart));
        assertEquals(price, cartService.create(cart).getPrice());
        assertEquals(discount, cartService.create(cart).getDiscount());
        assertEquals(cart.getProducts(), cartService.create(cart).getProducts());
    }

    @Test
    void calculateDiscount_shouldReturnNewDiscountValue() {
        double discount = 10;
        double price = 180;
        String product1Sku = "FAWCD-2035";
        String product2Sku = "VYVLA-7385";
        String product3Sku = "POERX-4320";

        var bundles = Arrays.asList(
                Bundle.builder()
                        .discount(0.5)
                        .products(Arrays.asList(product1Sku, product3Sku))
                        .id(UUID.randomUUID().toString())
                        .build(),
                Bundle.builder()
                        .discount(0.2)
                        .products(Arrays.asList(product2Sku, product3Sku))
                        .id(UUID.randomUUID().toString())
                        .build()
        );

        var cart =
                Cart.builder()
                        .products(Stream.of(product1Sku, product2Sku, product3Sku)
                                .collect(Collectors.toCollection(HashSet::new)))
                        .discount(discount)
                        .price(price)
                        .build();

        var product1 = Optional.of(
                Product.builder()
                        .sku(product1Sku)
                        .price(100)
                        .build());

        var product2 = Optional.of(
                Product.builder()
                        .sku(product2Sku)
                        .price(50)
                        .build());

        var product3 = Optional.of(
                Product.builder()
                        .sku(product3Sku)
                        .price(20)
                        .build());


        when(bundleService.getBundlesByProductIn(Collections.singleton(product3Sku)))
                .thenReturn(bundles);

        when(productService.get(product1Sku)).thenReturn(product1);
        when(productService.get(product2Sku)).thenReturn(product2);
        when(productService.get(product3Sku)).thenReturn(product3);

        assertEquals(60.0, cartService.calculateDiscount(cart, product3.get()));
    }

    @Test
    void calculateDiscount_shouldReturnDefaultDiscountValue() {
        double discount = 0;
        double price = 200;
        String product1Sku = "FAWCD-2035";
        String product2Sku = "VYVLA-7385";
        String product3Sku = "POERX-4320";
        String product4Sku = "KADWP-8584";
        String product5Sku = "NGDRF-6525";
        String product6Sku = "KQEWW-7405";

        List<Bundle> bundles = Collections.emptyList();

        var cart =
                Cart.builder()
                        .products(Stream.of(product1Sku, product2Sku, product3Sku, product4Sku, product5Sku, product6Sku)
                                .collect(Collectors.toCollection(HashSet::new)))
                        .discount(discount)
                        .price(price)
                        .build();

        var product6 = Optional.of(
                Product.builder()
                        .sku(product6Sku)
                        .price(10)
                        .build());

        when(bundleService.getBundlesByProductIn(Collections.singleton(product3Sku)))
                .thenReturn(bundles);

        assertEquals(12.0, cartService.calculateDiscount(cart, product6.get()));
    }

    @Test
    void calculateDiscount_shouldNotUpdateDiscountValue() {
        double discount = 50;
        double price = 200;
        String product1Sku = "FAWCD-2035";
        String product2Sku = "VYVLA-7385";
        String product3Sku = "POERX-4320";

        var bundles = Collections.singletonList(
                Bundle.builder()
                        .discount(0.1)
                        .products(Arrays.asList(product2Sku, product3Sku))
                        .id(UUID.randomUUID().toString())
                        .build()
        );

        var cart =
                Cart.builder()
                        .products(Stream.of(product1Sku, product2Sku, product3Sku)
                                .collect(Collectors.toCollection(HashSet::new)))
                        .discount(discount)
                        .price(price)
                        .build();


        var product1 = Optional.of(
                Product.builder()
                        .sku(product1Sku)
                        .price(100)
                        .build());

        var product2 = Optional.of(
                Product.builder()
                        .sku(product2Sku)
                        .price(50)
                        .build());

        var product3 = Optional.of(
                Product.builder()
                        .sku(product3Sku)
                        .price(20)
                        .build());

        when(bundleService.getBundlesByProductIn(Collections.singleton(product3Sku)))
                .thenReturn(bundles);

        when(productService.get(product1Sku)).thenReturn(product1);
        when(productService.get(product2Sku)).thenReturn(product2);
        when(productService.get(product3Sku)).thenReturn(product3);

        assertEquals(discount, cartService.calculateDiscount(cart, product3.get()));
    }

    @Test
    void update_shouldUpdateCart() {
        UUID cartId = UUID.randomUUID();
        double discount = 0;
        double discountAfter = 60;
        double price = 200;
        String product1Sku = "FAWCD-2035";
        String product2Sku = "VYVLA-7385";
        String product3Sku = "POERX-4320";

        var cartBefore =
                Cart.builder()
                        .id(cartId.toString())
                        .products(Stream.of(product1Sku, product2Sku)
                                .collect(Collectors.toCollection(HashSet::new)))
                        .discount(discount)
                        .price(price)
                        .build();

        var product1 = Optional.of(
                Product.builder()
                        .sku(product1Sku)
                        .price(100)
                        .build());

        var product2 = Optional.of(
                Product.builder()
                        .sku(product2Sku)
                        .price(50)
                        .build());

        var product3 = Optional.of(
                Product.builder()
                        .sku(product3Sku)
                        .price(20)
                        .build());

        var cartAfter =
                Cart.builder()
                        .id(cartId.toString())
                        .products(Stream.of(product1Sku, product2Sku, product3Sku)
                                .collect(Collectors.toCollection(HashSet::new)))
                        .discount(discountAfter)
                        .price(price + product3.get().getPrice())
                        .build();

        var bundles = Arrays.asList(
                Bundle.builder()
                        .discount(0.5)
                        .products(Arrays.asList(product1Sku, product3Sku))
                        .id(UUID.randomUUID().toString())
                        .build(),
                Bundle.builder()
                        .discount(0.2)
                        .products(Arrays.asList(product2Sku, product3Sku))
                        .id(UUID.randomUUID().toString())
                        .build()
        );

        when(bundleService.getBundlesByProductIn(Collections.singleton(product3Sku)))
                .thenReturn(bundles);

        when(productService.get(product1Sku)).thenReturn(product1);
        when(productService.get(product2Sku)).thenReturn(product2);
        when(productService.get(product3Sku)).thenReturn(product3);
        when(cartRepository.findById(cartId.toString())).thenReturn(Optional.of(cartBefore));
        when(cartRepository.save(cartAfter)).thenReturn(cartAfter);

        assertEquals(cartAfter, cartService.update(cartId, product3.get()));
    }
}