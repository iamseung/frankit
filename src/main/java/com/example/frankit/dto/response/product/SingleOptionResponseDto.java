package com.example.frankit.dto.response.product;

import com.example.frankit.dto.response.product.ProductOptionResponseDto;
import com.example.frankit.entity.product.OptionType;
import lombok.Setter;

@Setter
public class SingleOptionResponseDto extends ProductOptionResponseDto {
    private String value;

    public SingleOptionResponseDto(Long id, String productOptionName, Long additionalPrice, OptionType optionType, String value) {
        super(id, productOptionName, additionalPrice, optionType);
        setValue(value);
    }

    @Override
    public Object getValue() {
        return value;
    }
}