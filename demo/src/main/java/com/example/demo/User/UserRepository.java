package com.example.demo.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends JpaRepository<UserTable, Long> {
    boolean existsByEmail(String email);

    Optional<UserTable> findByEmail(String email);
}
