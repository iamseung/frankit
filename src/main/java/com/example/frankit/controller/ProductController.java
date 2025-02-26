package com.example.frankit.controller;

import com.example.frankit.dto.request.product.ProductDto;
import com.example.frankit.dto.request.product.ProductRequestDto;
import com.example.frankit.dto.response.ApiSuccessResponse;
import com.example.frankit.dto.security.CustomUserDetails;
import com.example.frankit.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ProductService productService;

    // 1. 상품 생성
    @PostMapping
    public ApiSuccessResponse createProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ProductRequestDto dto) {
        ProductDto product = productService.createProduct(dto.toDto(userDetails.toDto()));
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK", product);
    }

    // 2. 상품 업데이트
    @PutMapping("/{productId}")
    public ApiSuccessResponse updateProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long productId,
                                            @RequestBody ProductRequestDto dto ) {
        productService.updateProduct(productId, dto.toDto(userDetails.toDto()));
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK");
    }

    // 3. 상품 삭제
    @DeleteMapping("/{productId}")
    public ApiSuccessResponse deleteProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long productId) {
        productService.deleteProduct(productId, userDetails.toDto().getId());
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK");
    }

    // 4. 상품 조회 단건
    @GetMapping("/{productId}")
    public ApiSuccessResponse findProduct(@PathVariable Long productId) {
        ProductDto product = productService.findProduct(productId);
        System.out.println(product.toString());
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK", product);
    }

    // 5. 상품 조회 전체
    @GetMapping
    public ApiSuccessResponse findAllProduct(
            @PageableDefault(size = 10)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        List<ProductDto> allProductsWithOptions = productService.findAllProducts(pageable);
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK", allProductsWithOptions);
    }
}
