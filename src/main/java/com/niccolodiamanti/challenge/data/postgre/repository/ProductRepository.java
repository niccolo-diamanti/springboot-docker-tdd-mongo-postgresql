package com.niccolodiamanti.challenge.data.postgre.repository;

import com.niccolodiamanti.challenge.data.postgre.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
