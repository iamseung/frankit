package com.example.frankit.dto.request.product;

import com.example.frankit.dto.request.user.UserDto;
import lombok.Getter;

@Getter
public class ProductRequestDto {
    private String productName;
    private String description;
    private Long price;
    private Long quantity;

    public ProductDto toDto(UserDto userDto) {
        return ProductDto.of(userDto, productName, description, price, quantity);
    }
}
