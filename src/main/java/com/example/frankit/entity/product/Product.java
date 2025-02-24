package com.example.frankit.entity.product;

import com.example.frankit.dto.request.product.ProductDto;
import com.example.frankit.entity.user.User;
import com.example.frankit.exception.ProductErrorCode;
import com.example.frankit.exception.ProductException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.frankit.exception.ProductErrorCode.FAIL_PRODUCT_OPTION_CREATE_REQUEST;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
            @Index(name = "idx_product_name", columnList = "product_name")
})
public class Product extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "product_name", nullable = false)
    @Setter
    private String productName;

    @Column(name = "description", nullable = true)
    @Setter
    private String description;

    @Column(name = "price", nullable = false)
    @Setter
    private Long price;

    @Column(name = "quantity", nullable = false)
    @Setter
    private Long quantity;

    // 상품 옵션은 일대다 관계 매핑, 최대 3개의 옵션
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> productOptions = new ArrayList<>();

    @Builder
    public Product(String productName, String description, Long price, Long quantity, User user) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.user = user;
    }

    public static Product of(ProductDto dto) {
        return new Product(dto.getProductName(), dto.getDescription(), dto.getPrice(), dto.getQuantity(), dto.getUser().toEntity());
    }

    public void addOptions(List<ProductOption> options) {
        if(!isCanAddOptions(options)) {
            throw new ProductException(FAIL_PRODUCT_OPTION_CREATE_REQUEST, "옵션 제한 수량을 초과했습니다.");
        }

        for(ProductOption option : options) {
            option.setProduct(this);
            this.productOptions.add(option);
        }
    }

    public boolean isCanAddOptions(List<ProductOption> options) {
        return this.productOptions.size() + options.size() <= 3;
    }
}
