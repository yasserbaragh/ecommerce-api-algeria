package com.example.demo.Product;

import com.example.demo.Tag.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long id;
    private String name;
    private int stock;
    private double price;
    private String categoryName;
    private String description;
    private Double weight;
    private Integer warrantyMonths;
    private Set<String> tags;
}
