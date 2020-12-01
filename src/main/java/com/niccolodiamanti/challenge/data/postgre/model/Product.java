package com.niccolodiamanti.challenge.data.postgre.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "product")
public class Product {

    @Id
    private String sku;

    @Column(name = "price", nullable = false)
    private double price;
}
