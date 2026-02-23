package com.example.demo.Tag;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
class TagService {
    private final TagRepository tagRepository;

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

}
