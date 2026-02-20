package com.example.demo.Product;

import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getProducts() {
        return productRepository.findAllWithDetails();
    }

    @Transactional
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product addProduct(ProductDto productDTO) {
        if (productDTO.getCategoryName() == null || productDTO.getCategoryName().isEmpty()) {
            throw new IllegalArgumentException("Category must be provided");
        }

        // Find the category by name
        Category category = categoryRepository.findByName(productDTO.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category '" + productDTO.getCategoryName() + "' does not exist"
                ));

        // Map DTO to Product entity
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setCategory(category);

        return productRepository.save(product);
    }
}
