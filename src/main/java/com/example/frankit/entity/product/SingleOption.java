package com.example.frankit.entity.product;

import com.example.frankit.exception.ProductException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import static com.example.frankit.entity.product.OptionType.SINGLE;
import static com.example.frankit.exception.ProductErrorCode.FAIL_PRODUCT_OPTION_CREATE_REQUEST;

@Entity
@Getter
@DiscriminatorValue(value = "SINGLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SingleOption extends ProductOption {

    private String value;

    @Override
    public OptionType getOptionType() {
        return SINGLE;
    }

    @Builder
    public SingleOption(String name, Long price, Object value) {
        super(name, price, SINGLE);

        if(isNotValidValue(value)) {
            throw new ProductException(FAIL_PRODUCT_OPTION_CREATE_REQUEST, "올바르지 않은 옵션 정보가 입력되었습니다.");
        }

        setValue((String) value);
    }

    @Override
    public boolean isNotValidValue(Object value) {
        return !(value instanceof String);
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void updateValue(Object value) {
        if (isNotValidValue(value)) {
            throw new ProductException(FAIL_PRODUCT_OPTION_CREATE_REQUEST, "SINGLE 옵션에는 문자열 값만 입력 가능합니다.");
        }

        setValue((String) value);
    }
}