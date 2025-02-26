package com.example.frankit.dto.request.product;

import com.example.frankit.dto.request.user.UserDto;
import com.example.frankit.entity.product.Product;
import com.example.frankit.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long id;
    @JsonIgnore
    private UserDto user;
    private String productName;
    private String description;
    private Long price;
    private Long quantity;
    private List<ProductOptionDto> productOptions;

    public ProductDto(Long id, UserDto user, String productName, String description, Long price, Long quantity) {
        this.id = id;
        this.user = user;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public static ProductDto of(Long id, UserDto user, String productName, String description, Long price, Long quantity) {
        return new ProductDto(id, user, productName, description, price, quantity);
    }

    public static ProductDto of(UserDto user, String productName, String description, Long price, Long quantity) {
        return new ProductDto(null, user, productName, description, price, quantity);
    }

    public Product toEntity(User user) {
        return new Product(productName, description, price, quantity, user);
    }

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getId(),
                UserDto.from(product.getUser()),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getProductOptions().stream().map(ProductOptionDto::from).toList()
        );
    }
}
