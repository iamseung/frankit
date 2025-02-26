package com.example.frankit.service;

import com.example.frankit.dto.request.product.ProductOptionRequestDto;
import com.example.frankit.dto.response.product.ProductOptionResponseDto;
import com.example.frankit.dto.request.product.ProductOptionsRequestDto;
import com.example.frankit.entity.product.Product;
import com.example.frankit.entity.product.ProductOption;
import com.example.frankit.entity.user.User;
import com.example.frankit.exception.ProductException;
import com.example.frankit.exception.UserException;
import com.example.frankit.repository.ProductOptionRepository;
import com.example.frankit.repository.ProductRepository;
import com.example.frankit.repository.UserRepository;
import com.example.frankit.util.OptionParser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.frankit.exception.ProductErrorCode.NOT_EXIST_PRODUCT;
import static com.example.frankit.exception.UserErrorCode.IS_FORBIDDEN_ACCESS;

@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionRepository optionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OptionParser optionParser;

    @Transactional
    public void createProductOption(Long productId, ProductOptionsRequestDto dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(NOT_EXIST_PRODUCT, "상품이 존재하지 않습니다. productId : %s".formatted(productId)));

        List<ProductOption> options = new ArrayList<>();

        for(ProductOptionRequestDto request : dto.getOptions()) {
            ProductOption option = optionParser.toEntity(request);
            option.setProduct(product);
            options.add(option);
        }

        product.addOptions(options);
    }

    @Transactional
    public void updateProductOption(Long optionId, ProductOptionRequestDto dto) {
        try {
            ProductOption option = optionRepository.getReferenceById(optionId);

            if(dto.getProductOptionName() != null) option.setProductOptionName(dto.getProductOptionName());
            if(dto.getAdditionalPrice() != null) option.setAdditionalPrice(dto.getAdditionalPrice());
            if(isCanUpdateProductOption(option, dto)) option.updateValue(dto.getValue());
        } catch (EntityNotFoundException e) {
            throw new ProductException(NOT_EXIST_PRODUCT, "상품 옵션이 존재하지 않습니다. product Id : %s".formatted(optionId));
        }
    }

    public boolean isCanUpdateProductOption(ProductOption option, ProductOptionRequestDto request) {
        if(request.getOptionType() == null || request.getValue() == null) return false;
        if(option.getOptionType() != request.getOptionType()) return false;
        return true;
    }

    @Transactional
    public void deleteOptionByOptionId(Long optionId, Long userId) {
        try {
            User user = userRepository.getReferenceById(userId);

            ProductOption productOption = optionRepository.getReferenceById(optionId);
            Product product = productOption.getProduct();

            validationUserAuth(product, user);

            optionRepository.deleteProductOptionById(optionId);

        } catch (EntityNotFoundException e) {
            throw new ProductException(NOT_EXIST_PRODUCT, "상품이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void deleteAllOptionByOptionId(Long productId, Long userId) {
        try {
            User user = userRepository.getReferenceById(userId);
            Product product = productRepository.getReferenceById(productId);

            validationUserAuth(product, user);

            optionRepository.deleteAllByProductId(productId);
        } catch (EntityNotFoundException e) {
            throw new ProductException(NOT_EXIST_PRODUCT, "상품이 존재하지 않습니다.");
        }
    }

    public ProductOptionResponseDto findOption(Long optionId) {
        return optionRepository.findById(optionId)
                .map(optionParser::from)
                .orElseThrow(() -> new ProductException(NOT_EXIST_PRODUCT, "옵션이 존재하지 않습니다. optionId : %s".formatted(optionId)));
    }

    public List<ProductOptionResponseDto> findAllOptions(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(NOT_EXIST_PRODUCT, "상품이 존재하지 않습니다. productId : %s".formatted(productId)));

        return product.getProductOptions().stream()
                .map(optionParser::from)
                .toList();
    }

    public void validationUserAuth(Product product, User user) {
        if(!product.getUser().equals(user)) {
            throw new UserException(IS_FORBIDDEN_ACCESS, "허용되지 않은 접근입니다.");
        }
    }
}
