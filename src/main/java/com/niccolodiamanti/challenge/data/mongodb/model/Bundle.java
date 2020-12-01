package com.niccolodiamanti.challenge.data.mongodb.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Document
public class Bundle {

    private String id;
    private List<String> products;
    private double discount;
}
