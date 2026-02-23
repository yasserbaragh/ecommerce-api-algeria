package com.example.demo.Product;

import com.example.demo.Tag.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProductDto {
    private String name;
    private int stock;
    private double price;
    private String categoryName;
    private String description;
    private Double weight;
    private Integer warrantyMonths;
    private Set<Tag> tags;
}
