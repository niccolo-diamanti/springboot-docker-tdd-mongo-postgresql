package com.niccolodiamanti.challenge.data.mongodb.repository;

import com.niccolodiamanti.challenge.container.MongoDbContainer;
import com.niccolodiamanti.challenge.data.mongodb.model.Cart;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = CartRepositoryTest.MongoDbInitializer.class)
class CartRepositoryTest {

    private static MongoDbContainer mongoDbContainer;

    @Autowired
    private CartRepository cartRepository;

    @BeforeAll
    public static void startContainer() {
        mongoDbContainer = new MongoDbContainer();
        mongoDbContainer.start();
    }

    @AfterEach
    public void clearDatabase() {
        cartRepository.deleteAll();
    }

    @Test
    void findById_shouldGetCart() {
        double price = 150.50;
        double discount = 50;
        String product1 = "FAWCD-2035";
        String product2 = "VYVLA-7385";

        var cart =
                Cart.builder()
                        .id(UUID.randomUUID().toString())
                        .products(Stream.of(product1, product2)
                                .collect(Collectors.toCollection(HashSet::new)))
                        .price(price)
                        .discount(discount)
                        .build();
        var savedCart = cartRepository.save(cart);
        assertEquals(
                Optional.of(savedCart),
                cartRepository.findById(savedCart.getId())
        );
        assertEquals(savedCart.getPrice(), price);
        assertEquals(savedCart.getDiscount(), discount);
        assertTrue(savedCart.getProducts().contains(product1));
        assertTrue(savedCart.getProducts().contains(product2));
    }

    @Test
    void findById_shouldReturnEmptyIfCartNotFound() {
        assertEquals(
                Optional.empty(),
                cartRepository.findById(UUID.randomUUID().toString())
        );
    }

    public static class MongoDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.data.mongodb.host=" + mongoDbContainer.getContainerIpAddress(),
                    "spring.data.mongodb.port=" + mongoDbContainer.getPort()
            );
            values.applyTo(configurableApplicationContext);
        }
    }
}