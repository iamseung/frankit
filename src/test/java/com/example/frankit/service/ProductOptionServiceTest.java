package com.example.frankit.service;

import com.example.frankit.dto.request.product.ProductOptionRequestDto;
import com.example.frankit.dto.request.product.ProductOptionsRequestDto;
import com.example.frankit.dto.request.product.SelectOptionRequestDto;
import com.example.frankit.dto.request.product.SingleOptionRequestDto;
import com.example.frankit.dto.request.user.UserDto;
import com.example.frankit.dto.response.product.ProductOptionResponseDto;
import com.example.frankit.dto.response.product.SelectOptionResponseDto;
import com.example.frankit.dto.response.product.SingleOptionResponseDto;
import com.example.frankit.entity.product.OptionType;
import com.example.frankit.entity.product.Product;
import com.example.frankit.entity.product.ProductOption;
import com.example.frankit.entity.product.SingleOption;
import com.example.frankit.entity.user.User;
import com.example.frankit.exception.ProductErrorCode;
import com.example.frankit.exception.ProductException;
import com.example.frankit.exception.UserErrorCode;
import com.example.frankit.exception.UserException;
import com.example.frankit.repository.ProductOptionRepository;
import com.example.frankit.repository.ProductRepository;
import com.example.frankit.repository.UserRepository;
import com.example.frankit.util.OptionParser;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.frankit.entity.product.OptionType.SINGLE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("비즈니스 로직 - 상품 옵션")
class ProductOptionServiceTest {

    @InjectMocks private ProductOptionService sut; // System Under Test
    @Mock private ProductOptionRepository optionRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;
    @Mock private OptionParser optionParser;

    @DisplayName("상품 옵션 정보를 입력하면, 옵션을 생성한다.")
    @Test
    void givenProductOptionInfo_whenCreatingOption_thenSavesOption() {
        // Given
        Long productId = 1L;
        Product product = createProduct(createUser());
        ProductOptionsRequestDto dto = createProductOptionsRequestDto();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(optionParser.toEntity(any(ProductOptionRequestDto.class))).willReturn(createProductOption(product));

        // When
        assertThatCode(() -> sut.createProductOption(productId, dto)).doesNotThrowAnyException();

        // Then
        then(productRepository).should().findById(productId);
        then(optionParser).should(times(dto.getOptions().size())).toEntity(any(ProductOptionRequestDto.class));
    }

    @DisplayName("존재하지 않는 상품에 옵션을 추가하려 하면, 예외를 던진다.")
    @Test
    void givenNonexistentProduct_whenCreatingOption_thenThrowsException() {
        // Given
        Long productId = 1L;
        ProductOptionsRequestDto dto = createProductOptionsRequestDto();
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> sut.createProductOption(productId, dto))
                .isInstanceOf(ProductException.class)
                .hasMessageContaining("상품이 존재하지 않습니다.")
                .extracting("errorCode").isEqualTo(ProductErrorCode.NOT_EXIST_PRODUCT);
    }

    @DisplayName("상품 옵션 정보를 입력하면, 옵션을 수정한다.")
    @Test
    void givenProductOptionInfo_whenUpdatingOption_thenUpdatesOption() {
        // Given
        Long optionId = 1L;
        ProductOptionRequestDto dto = createProductOptionRequestDto();
        ProductOption option = createProductOption(createProduct(createUser()));

        given(optionRepository.getReferenceById(optionId)).willReturn(option);

        // When
        sut.updateProductOption(optionId, dto);

        // Then
        assertThat(option)
                .hasFieldOrPropertyWithValue("productOptionName", dto.getProductOptionName())
                .hasFieldOrPropertyWithValue("additionalPrice", dto.getAdditionalPrice());

        then(optionRepository).should().getReferenceById(optionId);
    }

    @DisplayName("존재하지 않는 옵션을 수정하려 하면, 예외를 던진다.")
    @Test
    void givenNonexistentOption_whenUpdatingOption_thenThrowsException() {
        // Given
        Long optionId = 1L;
        ProductOptionRequestDto dto = createProductOptionRequestDto();
        given(optionRepository.getReferenceById(optionId)).willThrow(EntityNotFoundException.class);

        // When & Then
        assertThatThrownBy(() -> sut.updateProductOption(optionId, dto))
                .isInstanceOf(ProductException.class)
                .hasMessageContaining("상품 옵션이 존재하지 않습니다.")
                .extracting("errorCode").isEqualTo(ProductErrorCode.NOT_EXIST_PRODUCT);
    }

    @DisplayName("옵션 ID와 사용자 ID를 입력하면, 옵션을 삭제한다.")
    @Test
    void givenOptionIdAndUserId_whenDeletingOption_thenDeletesOption() {
        // Given
        Long optionId = 1L;
        Long userId = 1L;

        User user = createUser();
        Product product = createProduct(user);
        ProductOption option = createProductOption(product);
        option.setProduct(product);

        given(userRepository.getReferenceById(userId)).willReturn(user);
        given(optionRepository.getReferenceById(optionId)).willReturn(option);
        willDoNothing().given(optionRepository).deleteProductOptionById(optionId);

        // When
        sut.deleteOptionByOptionId(optionId, userId);

        // Then
        then(userRepository).should().getReferenceById(userId);
        then(optionRepository).should().getReferenceById(optionId);
        then(optionRepository).should().deleteProductOptionById(optionId);
    }

    @DisplayName("존재하지 않는 옵션을 삭제하려 하면, 예외를 던진다.")
    @Test
    void givenNonexistentOption_whenDeletingOption_thenThrowsException() {
        // Given
        Long optionId = 1L;
        Long userId = 1L;
        given(userRepository.getReferenceById(userId)).willReturn(createUser());
        given(optionRepository.getReferenceById(optionId)).willThrow(EntityNotFoundException.class);

        // When & Then
        assertThatThrownBy(() -> sut.deleteOptionByOptionId(optionId, userId))
                .isInstanceOf(ProductException.class)
                .hasMessageContaining("상품이 존재하지 않습니다.")
                .extracting("errorCode").isEqualTo(ProductErrorCode.NOT_EXIST_PRODUCT);
    }

    @DisplayName("상품 ID와 사용자 ID를 입력하면, 해당 상품의 모든 옵션을 삭제한다.")
    @Test
    void givenProductIdAndUserId_whenDeletingAllOptions_thenDeletesAllOptions() {
        // Given
        Long productId = 1L;
        Long userId = 1L;
        User user = createUser();
        Product product = createProduct(user);

        given(userRepository.getReferenceById(userId)).willReturn(user);
        given(productRepository.getReferenceById(productId)).willReturn(product);
        willDoNothing().given(optionRepository).deleteAllByProductId(productId);

        // When
        sut.deleteAllOptionByOptionId(productId, userId);

        // Then
        then(userRepository).should().getReferenceById(userId);
        then(productRepository).should().getReferenceById(productId);
        then(optionRepository).should().deleteAllByProductId(productId);
    }

    @DisplayName("옵션 ID로 옵션을 조회하면, 옵션을 반환한다.")
    @Test
    void givenOptionId_whenFindingOption_thenReturnsOption() {
        // Given
        Long optionId = 1L;
        ProductOption option = createProductOption(createProduct(createUser()));
        ProductOptionResponseDto responseDto = createProductOptionResponseDto();

        given(optionRepository.findById(optionId)).willReturn(Optional.of(option));
        given(optionParser.from(option)).willReturn(responseDto);

        // When
        ProductOptionResponseDto result = sut.findOption(optionId);

        // Then
        assertThat(result).isEqualTo(responseDto);
        then(optionRepository).should().findById(optionId);
    }

    @DisplayName("존재하지 않는 옵션을 조회하려 하면, 예외를 던진다.")
    @Test
    void givenNonexistentOption_whenFindingOption_thenThrowsException() {
        // Given
        Long optionId = 1L;
        given(optionRepository.findById(optionId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> sut.findOption(optionId))
                .isInstanceOf(ProductException.class)
                .hasMessageContaining("옵션이 존재하지 않습니다.")
                .extracting("errorCode").isEqualTo(ProductErrorCode.NOT_EXIST_PRODUCT);
    }

    @DisplayName("상품 ID로 모든 옵션을 조회하면, 옵션 리스트를 반환한다.")
    @Test
    void givenProductId_whenFindingAllOptions_thenReturnsOptionList() {
        // Given
        Long productId = 1L;
        Product product = createProduct(createUser());
        product.addOptions(List.of(createProductOption(product)));

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // When
        List<ProductOptionResponseDto> result = sut.findAllOptions(productId);

        // Then
        assertThat(result).hasSize(1);
        then(productRepository).should().findById(productId);
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

    private Product createProduct(User user) {
        return new Product( "상품명", "상품 설명", 10000L, 100L, user);
    }

    private ProductOption createProductOption(Product product) {
        return SingleOption.builder()
                .name("테스트 옵션")
                .price(10000L)
                .value("RED")
                .build();
    }

    private ProductOptionRequestDto createProductOptionRequestDto() {
        return new SingleOptionRequestDto("옵션명", 500L, SINGLE, "옵션값");
    }

    private ProductOptionRequestDto createSelectProductOptionRequestDto() {
        return new SelectOptionRequestDto("옵션명", 500L, OptionType.SELECT, Set.of("옵션값1", "옵션값2"));
    }

    private ProductOptionResponseDto createProductOptionResponseDto() {
        return new SingleOptionResponseDto(1L, "옵션명", 500L, SINGLE, "옵션값");
    }

    private ProductOptionResponseDto createSelectProductOptionResponseDto() {
        return new SelectOptionResponseDto(1L, "옵션명", 500L, OptionType.SELECT, Set.of("옵션값1", "옵션값2"));
    }

    private ProductOptionsRequestDto createProductOptionsRequestDto() {
        return new ProductOptionsRequestDto(List.of(createProductOptionRequestDto()));
    }
}