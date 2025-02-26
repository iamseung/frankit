package com.example.frankit.service;

import com.example.frankit.dto.request.product.ProductDto;
import com.example.frankit.dto.request.user.UserDto;
import com.example.frankit.entity.product.Product;
import com.example.frankit.entity.user.User;
import com.example.frankit.exception.ProductException;
import com.example.frankit.exception.UserErrorCode;
import com.example.frankit.exception.UserException;
import com.example.frankit.repository.ProductRepository;
import com.example.frankit.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.frankit.exception.ProductErrorCode.NOT_EXIST_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("비즈니스 로직 - 상품")
class ProductServiceTest {

    @InjectMocks private ProductService sut;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;

    @DisplayName("상품 정보를 입력하면, 상품을 생성한다.")
    @Test
    void givenProductInfo_whenCreatingProduct_thenSavesProduct() {
        // Given
        ProductDto dto = createProductDto();
        User user = createUser();
        given(userRepository.getReferenceById(dto.getUser().getId())).willReturn(user);
        given(productRepository.save(any(Product.class))).willReturn(dto.toEntity(user));

        // When
        ProductDto result = sut.createProduct(dto);

        // Then
        assertThat(result).isEqualTo(dto);
        then(userRepository).should().getReferenceById(dto.getUser().getId());
        then(productRepository).should().save(any(Product.class));
    }

    @DisplayName("상품 정보를 입력하면, 상품을 수정한다.")
    @Test
    void givenModifiedProductInfo_whenUpdatingProduct_thenUpdatesProduct() {
        // Given
        Long productId = 1L;
        ProductDto dto = createProductDto("새 상품명", "새 설명", 2000L, 20L);
        User user = createUser();
        Product product = createProduct(user);

        given(userRepository.getReferenceById(dto.getUser().getId())).willReturn(user);
        given(productRepository.getReferenceById(productId)).willReturn(product);

        // When
        sut.updateProduct(productId, dto);

        // Then
        assertThat(product)
                .hasFieldOrPropertyWithValue("productName", dto.getProductName())
                .hasFieldOrPropertyWithValue("description", dto.getDescription())
                .hasFieldOrPropertyWithValue("price", dto.getPrice())
                .hasFieldOrPropertyWithValue("quantity", dto.getQuantity());

        then(userRepository).should().getReferenceById(dto.getUser().getId());
        then(productRepository).should().getReferenceById(productId);
    }

    @DisplayName("존재하지 않는 상품을 수정하려 하면, 예외를 던진다.")
    @Test
    void givenNonexistentProduct_whenUpdatingProduct_thenThrowsException() {
        // Given
        Long productId = 1L;
        ProductDto dto = createProductDto();
        given(userRepository.getReferenceById(dto.getUser().getId())).willReturn(createUser());
        given(productRepository.getReferenceById(productId)).willThrow(EntityNotFoundException.class);

        // When & Then
        assertThatThrownBy(() -> sut.updateProduct(productId, dto))
                .isInstanceOf(ProductException.class)
                .hasMessageContaining("상품이 존재하지 않습니다.")
                .extracting("errorCode").isEqualTo(NOT_EXIST_PRODUCT);
    }

    @DisplayName("상품 ID와 사용자 ID를 입력하면, 상품을 삭제한다.")
    @Test
    void givenProductIdAndUserId_whenDeletingProduct_thenDeletesProduct() {
        // Given
        Long productId = 1L;
        Long userId = 1L;
        User user = createUser();
        Product product = createProduct(user);

        given(userRepository.getReferenceById(userId)).willReturn(user);
        given(productRepository.getReferenceById(productId)).willReturn(product);
        willDoNothing().given(productRepository).deleteByIdAndUserId(productId, userId);

        // When
        sut.deleteProduct(productId, userId);

        // Then
        then(userRepository).should().getReferenceById(userId);
        then(productRepository).should().getReferenceById(productId);
        then(productRepository).should().deleteByIdAndUserId(productId, userId);
    }

    @DisplayName("존재하지 않는 상품을 삭제하려 하면, 예외를 던진다.")
    @Test
    void givenNonexistentProduct_whenDeletingProduct_thenThrowsException() {
        // Given
        Long productId = 1L;
        Long userId = 1L;
        given(userRepository.getReferenceById(userId)).willReturn(createUser());
        given(productRepository.getReferenceById(productId)).willThrow(EntityNotFoundException.class);

        // When & Then
        assertThatThrownBy(() -> sut.deleteProduct(productId, userId))
                .isInstanceOf(ProductException.class)
                .hasMessageContaining("상품이 존재하지 않습니다.")
                .extracting("errorCode").isEqualTo(NOT_EXIST_PRODUCT);
    }

    @DisplayName("상품 단건 조회 시, 상품을 반환한다.")
    @Test
    void givenProductId_whenFindingProduct_thenReturnsProduct() {
        // Given
        Long productId = 1L;
        Product product = createProduct(createUser());
        given(productRepository.findProductWithOptionsAndUser(productId)).willReturn(Optional.of(product));

        // When
        ProductDto result = sut.findProduct(productId);

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("productName", product.getProductName())
                .hasFieldOrPropertyWithValue("description", product.getDescription())
                .hasFieldOrPropertyWithValue("price", product.getPrice())
                .hasFieldOrPropertyWithValue("quantity", product.getQuantity());

        then(productRepository).should().findProductWithOptionsAndUser(productId);
    }

    @DisplayName("존재하지 않는 상품을 조회하려 하면, 예외를 던진다.")
    @Test
    void givenNonexistentProduct_whenFindingProduct_thenThrowsException() {
        // Given
        Long productId = 1L;
        given(productRepository.findProductWithOptionsAndUser(productId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> sut.findProduct(productId))
                .isInstanceOf(ProductException.class)
                .hasMessageContaining("상품이 존재하지 않습니다.")
                .extracting("errorCode").isEqualTo(NOT_EXIST_PRODUCT);

        then(productRepository).should().findProductWithOptionsAndUser(productId);
    }

    @DisplayName("모든 상품을 조회하면, 상품 목록을 반환한다.")
    @Test
    void givenPageable_whenFindingAllProducts_thenReturnsProductList() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        Product product = createProduct(createUser());
        given(productRepository.findAll(pageable)).willReturn(Page.empty().map(entity -> product));

        // When
        List<ProductDto> result = sut.findAllProducts(pageable);

        // Then
        assertThat(result).isEmpty();
        then(productRepository).should().findAll(pageable);
    }

    @DisplayName("사용자가 권한이 없으면, 예외를 던진다.")
    @Test
    void givenUnauthorizedUser_whenValidatingUserAuth_thenThrowsException() {
        // Given
        Product product = createProduct(createUser());
        User otherUser = User.of(101L, "test1@test.com", "password");

        // When & Then
        assertThatThrownBy(() -> sut.validationUserAuth(product, otherUser))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("허용되지 않은 접근입니다.")
                .extracting("errorCode").isEqualTo(UserErrorCode.IS_FORBIDDEN_ACCESS);
    }

    private User createUser() {
        return User.of(100L, "test@test.com", "password");
    }

    private UserDto createUserDto() {
        return UserDto.of(100L, "test@test.com", "password");
    }

    private Product createProduct(User user) {
        return new Product( "상품명", "상품 설명", 10000L, 100L, user);
    }

    private ProductDto createProductDto() {
        return createProductDto("상품명", "상품 설명", 10000L, 100L);
    }

    private ProductDto createProductDto(String productName, String description, Long price, Long quantity) {
        return ProductDto.of(1L, createUserDto(), productName, description, price, quantity);
    }
}