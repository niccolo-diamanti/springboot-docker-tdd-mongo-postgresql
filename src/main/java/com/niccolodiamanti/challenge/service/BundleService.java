package com.niccolodiamanti.challenge.service;

import com.niccolodiamanti.challenge.data.mongodb.model.Bundle;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface BundleService {

    Optional<Bundle> get(UUID id);

    List<Bundle> getBundlesByProductIn(Set<String> products);
}
