package com.example.frankit.util;

import com.example.frankit.dto.request.product.ProductOptionRequestDto;
import com.example.frankit.dto.response.product.ProductOptionResponseDto;
import com.example.frankit.dto.response.product.SelectOptionResponseDto;
import com.example.frankit.dto.response.product.SingleOptionResponseDto;
import com.example.frankit.entity.product.ProductOption;
import com.example.frankit.entity.product.SelectOption;
import com.example.frankit.entity.product.SingleOption;
import com.example.frankit.exception.ProductException;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.example.frankit.exception.ProductErrorCode.FAIL_PRODUCT_OPTION_CREATE_REQUEST;

@Component
public class OptionParser {

    public ProductOption toEntity(ProductOptionRequestDto optionRequest) {
        return switch (optionRequest.getOptionType()) {
            case SINGLE -> new SingleOption(optionRequest.getProductOptionName(), optionRequest.getAdditionalPrice(), optionRequest.getValue());
            case SELECT -> new SelectOption(optionRequest.getProductOptionName(), optionRequest.getAdditionalPrice(), optionRequest.getValue());
            default -> throw new ProductException(FAIL_PRODUCT_OPTION_CREATE_REQUEST, "지원되지 않는 옵션 타입입니다: " + optionRequest.getOptionType());
        };
    }

    public ProductOptionResponseDto from(ProductOption option) {
        return switch (option.getOptionType()) {
            case SINGLE -> new SingleOptionResponseDto(option.getId(), option.getProductOptionName(), option.getAdditionalPrice(), option.getOptionType(), (String) option.getValue());
            case SELECT -> new SelectOptionResponseDto(option.getId(), option.getProductOptionName(), option.getAdditionalPrice(), option.getOptionType(), (Set<String>) option.getValue());
            default -> throw new ProductException(FAIL_PRODUCT_OPTION_CREATE_REQUEST, "지원되지 않는 옵션 타입입니다: " + option.getOptionType());
        };
    }
}
