package com.example.frankit.repository;

import com.example.frankit.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.productOptions po " +
            "LEFT JOIN FETCH po.product " +
            "WHERE p.id = :productId")
    Optional<Product> findProductWithOptionsAndUser(@Param("productId") Long productId);

    void deleteByIdAndUserId(Long productId, Long userId);
}
