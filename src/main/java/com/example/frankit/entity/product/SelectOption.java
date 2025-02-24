package com.example.frankit.entity.product;

import com.example.frankit.exception.ProductException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static com.example.frankit.entity.product.OptionType.SELECT;
import static com.example.frankit.exception.ProductErrorCode.FAIL_PRODUCT_OPTION_CREATE_REQUEST;

@Entity
@Getter
@Setter
@DiscriminatorValue(value = "SELECT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectOption extends ProductOption {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "select_option_values", joinColumns = @JoinColumn(name = "option_id"))
    @Column(name = "value")
    private Set<String> values;

    @Override
    public OptionType getOptionType() {
        return SELECT;
    }

    @Builder
    public SelectOption(String name, Long price, Object value) {
        super(name, price, SELECT);

        if(isNotValidValue(value)) {
            throw new ProductException(FAIL_PRODUCT_OPTION_CREATE_REQUEST, "올바르지 않은 옵션 정보가 입력되었습니다.");
        }

        setValues((Set<String>) value);
    }

    @Override
    public Object getValue() {
        return values;
    }

    @Override
    public boolean isNotValidValue(Object value) {
        return !(value instanceof Set<?>);
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }

    @Override
    public void updateValue(Object value) {
        if (isNotValidValue(value)) {
            throw new ProductException(FAIL_PRODUCT_OPTION_CREATE_REQUEST, "SELECT 옵션에는 Set<String> 값만 입력 가능합니다.");
        }

        setValues((Set<String>) value);
    }
}
