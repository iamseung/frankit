package com.example.frankit.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductApiResponseDto {
    private int resultCode;
    private String comment;
}
