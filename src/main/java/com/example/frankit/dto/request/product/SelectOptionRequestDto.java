package com.example.frankit.dto.request.product;

import com.example.frankit.entity.product.OptionType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SelectOptionRequestDto extends ProductOptionRequestDto {
    private Set<String> values;

    @Override
    public Object getValue() {
        return values;
    }

    public SelectOptionRequestDto(String name, Long price, OptionType optionType, Set<String> values) {
        super(name, price,optionType);
        this.values = values;
    }


}