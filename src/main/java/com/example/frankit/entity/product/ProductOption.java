package com.example.frankit.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "product_option",
        indexes = {
                @Index(name = "idx_option_product", columnList = "product_id"),
                @Index(name = "idx_option_type", columnList = "option_type")
        })
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "option_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    @Column(name = "product_option_name")
    private String productOptionName;

    private Long additionalPrice;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @Setter private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_type", insertable = false, updatable = false)
    private OptionType optionType;

    public ProductOption(String productOptionName, Long additionalPrice, OptionType optionType) {
        this.productOptionName = productOptionName;
        this.additionalPrice = additionalPrice;
        this.optionType = optionType;
    }

    public abstract OptionType getOptionType();
    public abstract Object getValue();
    public abstract void updateValue(Object value);
    public abstract boolean isNotValidValue(Object value);
}
