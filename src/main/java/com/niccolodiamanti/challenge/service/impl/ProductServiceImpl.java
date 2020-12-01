package com.niccolodiamanti.challenge.service.impl;

import com.niccolodiamanti.challenge.data.postgre.model.Product;
import com.niccolodiamanti.challenge.data.postgre.repository.ProductRepository;
import com.niccolodiamanti.challenge.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository _productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        _productRepository = productRepository;
    }

    @Override
    public Optional<Product> get(String id) {
        return _productRepository.findById(id);
    }
}
