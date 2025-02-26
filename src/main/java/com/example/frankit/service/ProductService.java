package com.example.frankit.service;

import com.example.frankit.dto.request.product.ProductDto;
import com.example.frankit.entity.product.Product;
import com.example.frankit.entity.user.User;
import com.example.frankit.exception.ProductException;
import com.example.frankit.exception.UserException;
import com.example.frankit.repository.ProductRepository;
import com.example.frankit.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.frankit.exception.ProductErrorCode.NOT_EXIST_PRODUCT;
import static com.example.frankit.exception.UserErrorCode.IS_FORBIDDEN_ACCESS;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProductDto createProduct(ProductDto dto) {
        User user = userRepository.getReferenceById(dto.getUser().getId());
        Product product = dto.toEntity(user);
        productRepository.save(product);
        return dto;
    }

    @Transactional
    public void updateProduct(Long productId, ProductDto dto) {
        try {
            User user = userRepository.getReferenceById(dto.getUser().getId());
            Product product = productRepository.getReferenceById(productId);

            validationUserAuth(product, user);

            if(dto.getProductName() != null) product.setProductName(dto.getProductName());
            if(dto.getDescription() != null) product.setDescription(dto.getDescription());
            if(dto.getPrice() != null) product.setPrice(dto.getPrice());
            if(dto.getQuantity() != null) product.setQuantity(dto.getQuantity());
        } catch (EntityNotFoundException e) {
            throw new ProductException(NOT_EXIST_PRODUCT, "상품이 존재하지 않습니다. product Id : %s".formatted(productId));
        }
    }

    @Transactional
    public void deleteProduct(Long productId, Long userId) {
        try {
            User user = userRepository.getReferenceById(userId);
            Product product = productRepository.getReferenceById(productId);

            validationUserAuth(product, user);

            productRepository.deleteByIdAndUserId(productId, userId);
        } catch (EntityNotFoundException e) {
            throw new ProductException(NOT_EXIST_PRODUCT, "상품이 존재하지 않습니다. product Id : %s".formatted(productId));
        }
    }

    // 단건 조회
    public ProductDto findProduct(Long productId) {
        return productRepository.findProductWithOptionsAndUser(productId)
                .map(ProductDto::from)
                .orElseThrow(() -> new ProductException(NOT_EXIST_PRODUCT, "상품이 존재하지 않습니다. product Id : %s".formatted(productId)));
    }

    // 모든 조회
    public List<ProductDto> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .stream()
                .map(ProductDto::from)
                .toList();
    }

    public void validationUserAuth(Product product, User user) {
        if(!product.getUser().equals(user)) {
            throw new UserException(IS_FORBIDDEN_ACCESS, "허용되지 않은 접근입니다.");
        }
    }
}
