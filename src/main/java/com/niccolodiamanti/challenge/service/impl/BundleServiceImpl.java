package com.niccolodiamanti.challenge.service.impl;

import com.niccolodiamanti.challenge.data.mongodb.model.Bundle;
import com.niccolodiamanti.challenge.data.mongodb.repository.BundleRepository;
import com.niccolodiamanti.challenge.service.BundleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class BundleServiceImpl implements BundleService {

    private final BundleRepository _bundleRepository;

    public BundleServiceImpl(BundleRepository bundleRepository) {
        _bundleRepository = bundleRepository;
    }

    @Override
    public Optional<Bundle> get(UUID id) {
        return _bundleRepository.findById(id.toString());
    }

    @Override
    public List<Bundle> getBundlesByProductIn(Set<String> products) {
        return _bundleRepository.getAllByProductsIn(products);
    }
}
