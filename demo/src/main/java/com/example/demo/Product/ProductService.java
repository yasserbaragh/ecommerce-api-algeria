package com.example.demo.Product;

import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryRepository;
import com.example.demo.Tag.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getProducts() {
        return productRepository.findAllWithDetails();
    }

    @EntityGraph(attributePaths = {"category", "tags"})
    public Product getProductById(Long id) {
        return productRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    @Transactional
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAllWithDetails();
        products.forEach(p -> {
            String name = p.getCategory().getName();
        });
        return products;
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
        for (Tag tag : productDTO.getTags()) {
            if (tag.getName() == null || tag.getName().isEmpty()) {
                throw new IllegalArgumentException("Tag name must be provided");
            }
        }

        // Map DTO to Product entity
        Product product = new Product();
        ProductDetails details = new ProductDetails();
        details.setDescription(productDTO.getDescription());
        details.setWeight(productDTO.getWeight());
        details.setWarrantyMonths(productDTO.getWarrantyMonths());
        // Set the back reference
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setCategory(category);
        product.setTags(productDTO.getTags());
        details.setProduct(product);
        product.setProductDetails(details);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDto productDTO) {
        Product existingProduct = getProductById(id);

        if (productDTO.getCategoryName() != null && !productDTO.getCategoryName().isEmpty()) {
            Category category = categoryRepository.findByName(productDTO.getCategoryName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Category '" + productDTO.getCategoryName() + "' does not exist"
                    ));
            existingProduct.setCategory(category);
        }

        if (productDTO.getTags() != null) {
            for (Tag tag : productDTO.getTags()) {
                if (tag.getName() == null || tag.getName().isEmpty()) {
                    throw new IllegalArgumentException("Tag name must be provided");
                }
            }
            existingProduct.setTags(productDTO.getTags());
        }

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStock(productDTO.getStock());

        ProductDetails details = existingProduct.getProductDetails();
        if (details == null) {
            details = new ProductDetails();
            details.setProduct(existingProduct);
            existingProduct.setProductDetails(details);
        }
        details.setDescription(productDTO.getDescription());
        details.setWeight(productDTO.getWeight());
        details.setWarrantyMonths(productDTO.getWarrantyMonths());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setCategory(null);
        product.getTags().clear();
        productRepository.deleteById(id);
    }
}
