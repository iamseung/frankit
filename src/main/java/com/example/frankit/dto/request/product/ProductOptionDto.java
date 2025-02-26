package com.example.frankit.dto.request.product;

import com.example.frankit.entity.product.OptionType;
import com.example.frankit.entity.product.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductOptionDto {
    private String productOptionName;
    private Long additionalPrice;
    private OptionType optionType;
    private Object value;

    public static ProductOptionDto from(ProductOption option) {
        return new ProductOptionDto(
                option.getProductOptionName(),
                option.getAdditionalPrice(),
                option.getOptionType(),
                option.getValue()
        );
    }
}
