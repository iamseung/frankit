package com.example.frankit.dto.request.product;

import com.example.frankit.entity.product.OptionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "optionType", visible = true, defaultImpl = SingleOptionRequestDto.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleOptionRequestDto.class, name = "SINGLE"),
        @JsonSubTypes.Type(value = SelectOptionRequestDto.class, name = "SELECT")
})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ProductOptionRequestDto {

    private String productOptionName;
    private Long additionalPrice;
    private OptionType optionType;

    public abstract Object getValue();
}
