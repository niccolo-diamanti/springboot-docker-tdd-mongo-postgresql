package com.niccolodiamanti.challenge.service;

import com.niccolodiamanti.challenge.data.postgre.model.Product;
import com.niccolodiamanti.challenge.data.postgre.repository.ProductRepository;
import com.niccolodiamanti.challenge.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void get_shouldReturnTheProduct() {

        var product = Optional.of(
                Product.builder()
                        .sku("AQUTZ-3919")
                        .price(38.51)
                        .build());

        when(productRepository.findById(product.get().getSku()))
                .thenReturn(product);

        assertEquals(product, productService.get(product.get().getSku()));
    }

    @Test
    void get_shouldReturnOptionalEmptyIfNotFound() {
        var productSku = "AQUTZ-3919";
        when(productRepository.findById(productSku))
                .thenReturn(Optional.empty());

        assertTrue(productService.get(productSku).isEmpty());
    }
}