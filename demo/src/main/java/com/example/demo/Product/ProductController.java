package com.example.demo.Product;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // Constructor Injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/all")
    public List<Product> getAll() {
        return productService.getProducts();
    }

    @PostMapping("")
    public Product createProduct(@RequestBody ProductDto product) {
        return productService.addProduct(product);
    }

    @PutMapping("{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDto product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
