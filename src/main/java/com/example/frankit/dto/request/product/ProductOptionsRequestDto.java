package com.example.frankit.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductOptionsRequestDto {

    private List<ProductOptionRequestDto> options;

}
