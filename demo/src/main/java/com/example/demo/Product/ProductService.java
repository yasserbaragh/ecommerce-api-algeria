package com.example.demo.Product;

import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryRepository;
import com.example.demo.Tag.Tag;
import com.example.demo.Tag.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public List<Product> getProducts() {
        return productRepository.findAllWithDetails();
    }

    public Product getProductById(Long id) {
        return productRepository.findByIdWithCategoryAndTags(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    @Transactional
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAllWithDetails();
        products.forEach(p -> {
            String name = (p.getCategory() != null) ? p.getCategory().getName(): "No Category";
        });
        return products;
    }

    @Transactional
    public Product addProduct(ProductDto productDTO) {
        Product product = new Product();
        if (productDTO.getCategoryName() == null || productDTO.getCategoryName().isEmpty()) {
            throw new IllegalArgumentException("Category must be provided");
        }

        // Find the category by name
        Category category = categoryRepository.findByName(productDTO.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category '" + productDTO.getCategoryName() + "' does not exist"
                ));

        addTagToProd(productDTO, product);


        // Map DTO to Product entity

        ProductDetails details = new ProductDetails();
        details.setDescription(productDTO.getDescription());
        details.setWeight(productDTO.getWeight());
        details.setWarrantyMonths(productDTO.getWarrantyMonths());
        // Set the back reference
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setCategory(category);


        details.setProduct(product);
        product.setProductDetails(details);

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductDto productDTO) {
        Product existingProduct = getProductById(id);

        if (productDTO.getCategoryName() != null && !productDTO.getCategoryName().isEmpty()) {
            Category category = categoryRepository.findByName(productDTO.getCategoryName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Category '" + productDTO.getCategoryName() + "' does not exist"
                    ));
            existingProduct.setCategory(category);

        }

        addTagToProd(productDTO, existingProduct);

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

    private void addTagToProd(ProductDto productDTO, Product existingProduct) {
        if (productDTO.getTags() != null) {
            // 1. Clear the existing relationship
            existingProduct.getTags().clear();

            // 2. Find and add the new tags
            for (String tagName : productDTO.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseThrow(() -> new IllegalArgumentException("Tag '" + tagName + "' not found"));


                existingProduct.getTags().add(tag);
                tag.getProducts().add(existingProduct);
            }
        }
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setCategory(null);
        product.getTags().clear();
        productRepository.deleteById(id);
    }
}
