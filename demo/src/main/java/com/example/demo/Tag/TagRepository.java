package com.example.demo.Tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

}
