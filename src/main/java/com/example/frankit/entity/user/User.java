package com.example.frankit.entity.user;

import com.example.frankit.entity.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    @Setter private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Product> products = new ArrayList<>();

    private User(Long id, String userId, String password) {
        this.id = id;
        this.userId = userId;
        this.password = password;
    }

    @Builder
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public static User of(Long id, String userId, String password) {
        return new User(id, userId, password);
    }

    public static User of(String userId, String password) {
        return new User(null, userId, password);
    }
}