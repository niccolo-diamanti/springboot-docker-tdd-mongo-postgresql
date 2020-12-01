package com.niccolodiamanti.challenge.data.postgre.repository;

import com.niccolodiamanti.challenge.data.postgre.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    public void clearDatabase() {
        productRepository.deleteAll();
    }

    @Test
    void findById_shouldGetProduct() {
        var product =
                Product.builder()
                        .sku("AQUTZ-3919")
                        .price(38.51)
                        .build();
        var savedProduct = productRepository.save(product);
        assertEquals(
                Optional.of(savedProduct),
                productRepository.findById(savedProduct.getSku())
        );
    }

    @Test
    void findById_shouldReturnEmptyIfProductNotFound() {
        assertEquals(
                Optional.empty(),
                productRepository.findById("notfound")
        );
    }
}