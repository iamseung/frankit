package com.example.frankit.dto.response.product;

import com.example.frankit.entity.product.OptionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "optionType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleOptionResponseDto.class, name = "SINGLE"),
        @JsonSubTypes.Type(value = SelectOptionResponseDto.class, name = "SELECT")
})
public abstract class ProductOptionResponseDto {
    private Long id;
    private String productOptionName;
    private Long additionalPrice;
    private OptionType optionType;

    public ProductOptionResponseDto(Long id, String productOptionName, Long additionalPrice, OptionType optionType) {
        this.id = id;
        this.productOptionName = productOptionName;
        this.additionalPrice = additionalPrice;
        this.optionType = optionType;
    }

    public abstract Object getValue();
}
