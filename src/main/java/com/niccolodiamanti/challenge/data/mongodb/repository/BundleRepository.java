package com.niccolodiamanti.challenge.data.mongodb.repository;

import com.niccolodiamanti.challenge.data.mongodb.model.Bundle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Set;

public interface BundleRepository extends MongoRepository<Bundle, String> {

    @Query("{products:{$in: ?0}}")
    List<Bundle> getAllByProductsIn(Set<String> products);
}
