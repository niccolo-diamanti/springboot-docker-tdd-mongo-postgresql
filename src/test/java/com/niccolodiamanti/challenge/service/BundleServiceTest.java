package com.niccolodiamanti.challenge.service;

import com.niccolodiamanti.challenge.data.mongodb.model.Bundle;
import com.niccolodiamanti.challenge.data.mongodb.repository.BundleRepository;
import com.niccolodiamanti.challenge.service.impl.BundleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BundleServiceTest {

    @Mock
    private BundleRepository bundleRepository;

    @InjectMocks
    private BundleServiceImpl bundleService;

    @Test
    void get_shouldReturnTheBundle() {
        double discount = 0.3;
        String product1 = "FAWCD-2035";
        String product2 = "VYVLA-7385";

        var bundle = Optional.of(
                Bundle.builder()
                        .id(UUID.randomUUID().toString())
                        .products(Stream.of(product1, product2)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .discount(discount)
                        .build());

        when(bundleRepository.findById(bundle.get().getId()))
                .thenReturn(bundle);

        assertEquals(bundle, bundleService.get(UUID.fromString(bundle.get().getId())));
    }

    @Test
    void get_shouldReturnOptionalEmptyIfNotFound() {
        var bundleId = UUID.randomUUID();
        when(bundleRepository.findById(bundleId.toString()))
                .thenReturn(Optional.empty());

        assertTrue(bundleService.get(bundleId).isEmpty());
    }

    @Test
    void getBundlesByProductIn_shouldReturnProductBundles() {
        double discount = 0.3;
        String product1 = "FAWCD-2035";
        String product2 = "VYVLA-7385";
        String product3 = "OLKWE-9347";

        var bundles = Arrays.asList(
                Bundle.builder()
                        .id(UUID.randomUUID().toString())
                        .products(Stream.of(product1, product2)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .discount(discount)
                        .build(),
                Bundle.builder()
                        .id(UUID.randomUUID().toString())
                        .products(Stream.of(product1, product3)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .discount(discount)
                        .build());

        when(bundleRepository.getAllByProductsIn(Collections.singleton(product1)))
                .thenReturn(bundles);

        assertEquals(bundles, bundleService.getBundlesByProductIn(Collections.singleton(product1)));
    }

    @Test
    void getBundlesByProductIn_shouldReturnEmptyList() {
        String product = "YRELE-0483";

        when(bundleRepository.getAllByProductsIn(Collections.singleton(product)))
                .thenReturn(new ArrayList<>());

        assertEquals(new ArrayList<>(), bundleService.getBundlesByProductIn(Collections.singleton(product)));
    }
}