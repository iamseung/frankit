package com.example.frankit.dto.response.product;

import com.example.frankit.dto.response.product.ProductOptionResponseDto;
import com.example.frankit.entity.product.OptionType;
import lombok.Setter;

import java.util.Set;

@Setter
public class SelectOptionResponseDto extends ProductOptionResponseDto {
    private Set<String> values;

    public SelectOptionResponseDto(Long id, String productOptionName, Long additionalPrice, OptionType optionType, Set<String> value) {
        super(id, productOptionName, additionalPrice, optionType);
        setValues(value);
    }

    @Override
    public Object getValue() {
        return values;
    }
}