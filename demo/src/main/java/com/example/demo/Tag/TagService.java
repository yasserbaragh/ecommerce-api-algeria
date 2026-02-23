package com.example.demo.Tag;

import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
class TagService {
    private final TagRepository tagRepository;
    private final ProductRepository productRepository;

    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    @Transactional
    public Set<?> getProductsByTagId(Long id) {
        Tag tag = getTagById(id);
        return tag.getProducts();
    }

    public Tag addTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    public Tag updateTag(Long id, Tag updatedTag) {
        Tag tag = getTagById(id);
        tag.setName(updatedTag.getName());
        return tagRepository.save(tag);
    }
    public void deleteTag(Long id) {
        Tag tag = getTagById(id);
        tag.getProducts().clear();
        tagRepository.deleteById(id);
    }

    @Transactional
    public void addProductToTag(Long tagId, Long productId) {
        // 1. Find the Tag (Ensures it exists)
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        // 2. Find the existing Product (Ensures it exists)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3. Add to the relationship
        // Since Tag is the "Owner" (it has the @JoinTable), we add here
        tag.getProducts().add(product);

        // 4. Save the Tag
        tagRepository.save(tag);
    }

}
