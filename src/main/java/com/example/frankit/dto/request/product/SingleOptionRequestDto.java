package com.example.frankit.dto.request.product;

import com.example.frankit.entity.product.OptionType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SingleOptionRequestDto extends ProductOptionRequestDto {
    private String value;

    @Override
    public Object getValue() {
        return value;
    }

    public SingleOptionRequestDto(String name, Long price, OptionType optionType, String value) {
        super(name, price,optionType);
        this.value = value;
    }
}