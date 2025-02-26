package com.example.frankit.repository;

import com.example.frankit.entity.product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    void deleteProductOptionById(Long productOptionId);
    void deleteAllByProductId(Long productId);
    List<ProductOption> findAllByProductId(Long productId);

    @Query("SELECT po FROM ProductOption po LEFT JOIN FETCH po.product WHERE po.id = :id")
    Optional<ProductOption> findOptionWithProduct(@Param("id") Long id);
}
