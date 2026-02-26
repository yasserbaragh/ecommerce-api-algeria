package com.example.demo.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;


    protected UserTable() {}

    public UserTable(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
