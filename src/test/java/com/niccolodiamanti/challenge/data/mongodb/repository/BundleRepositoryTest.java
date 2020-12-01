package com.niccolodiamanti.challenge.data.mongodb.repository;

import com.niccolodiamanti.challenge.container.MongoDbContainer;
import com.niccolodiamanti.challenge.data.mongodb.model.Bundle;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = BundleRepositoryTest.MongoDbInitializer.class)
class BundleRepositoryTest {

    private static MongoDbContainer mongoDbContainer;

    @Autowired
    private BundleRepository bundleRepository;

    @BeforeAll
    public static void startContainer() {
        mongoDbContainer = new MongoDbContainer();
        mongoDbContainer.start();
    }

    @AfterEach
    public void clearDatabase() {
        bundleRepository.deleteAll();
    }

    @Test
    void findById_shouldGetBundle() {
        double discount = 0.3;
        String product1 = "FAWCD-2035";
        String product2 = "VYVLA-7385";

        var bundle =
                Bundle.builder()
                        .id(UUID.randomUUID().toString())
                        .products(Stream.of(product1, product2)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .discount(discount)
                        .build();
        var savedBundle = bundleRepository.save(bundle);
        assertEquals(
                Optional.of(savedBundle),
                bundleRepository.findById(savedBundle.getId())
        );
        assertEquals(savedBundle.getDiscount(), discount);
        assertTrue(savedBundle.getProducts().contains(product1));
        assertTrue(savedBundle.getProducts().contains(product2));
    }

    @Test
    void findById_shouldReturnEmptyIfBundleNotFound() {
        assertEquals(
                Optional.empty(),
                bundleRepository.findById(UUID.randomUUID().toString())
        );
    }

    @Test
    void getAllByProductsIn_shouldGetBundles() {
        double discount = 0.3;
        String product1 = "FAWCD-2035";
        String product2 = "VYVLA-7385";
        String product3 = "OLKWE-9347";
        String product4 = "RJKGN-5621";

        var bundle1 =
                Bundle.builder()
                        .id(UUID.randomUUID().toString())
                        .products(Stream.of(product1, product2)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .discount(discount)
                        .build();

        var bundle2 =
                Bundle.builder()
                        .id(UUID.randomUUID().toString())
                        .products(Stream.of(product1, product3)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .discount(discount)
                        .build();

        var bundle3 =
                Bundle.builder()
                        .id(UUID.randomUUID().toString())
                        .products(Stream.of(product3, product4)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .discount(discount)
                        .build();

        var savedBundles = Stream.of(bundle1, bundle2, bundle3)
                .collect(Collectors.toCollection(HashSet::new));
        bundleRepository.saveAll(savedBundles);

        var bundles = bundleRepository.getAllByProductsIn(Collections.singleton(product1));

        assertEquals(2, bundles.size());
        assertTrue(bundles.get(0).getProducts().contains(product1));
        assertTrue(bundles.get(1).getProducts().contains(product1));
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