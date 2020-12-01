package com.niccolodiamanti.challenge.data.mongodb.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@Document
public class Cart {

    private String id;
    private Set<String> products;
    private double price;
    private double discount;
}
