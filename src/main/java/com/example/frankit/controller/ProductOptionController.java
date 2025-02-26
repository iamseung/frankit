package com.example.frankit.controller;

import com.example.frankit.dto.request.product.ProductOptionRequestDto;
import com.example.frankit.dto.response.product.ProductOptionResponseDto;
import com.example.frankit.dto.request.product.ProductOptionsRequestDto;
import com.example.frankit.dto.response.ApiSuccessResponse;
import com.example.frankit.dto.security.CustomUserDetails;
import com.example.frankit.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/options")
public class ProductOptionController {

    private final ProductOptionService optionService;

    // 1. 상품 옵션 생성
    @PostMapping("/{productId}")
    public ApiSuccessResponse createOption(@PathVariable Long productId, @RequestBody ProductOptionsRequestDto request) {
        optionService.createProductOption(productId, request);
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK");
    }

    // 2. 상품 옵션 업데이트
    @PutMapping("/{optionId}")
    public ApiSuccessResponse updateOption(@AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long optionId, @RequestBody ProductOptionRequestDto dto) {
        optionService.updateProductOption(optionId, dto);
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK");
    }

    // 3. 상품 옵션 삭제
    @DeleteMapping("/{optionId}")
    public ApiSuccessResponse deleteOption(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Long optionId) {
        optionService.deleteOptionByOptionId(optionId, userDetails.toDto().getId());
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK");
    }

    // 4. 상품 옵션 전체 삭제
    @DeleteMapping("/{productId}/all")
    public ApiSuccessResponse deleteAllOptions(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Long productId) {
        optionService.deleteAllOptionByOptionId(productId, userDetails.toDto().getId());
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK");
    }

    // 4. 상품 옵션 조회 단건
    @GetMapping("/{optionId}")
    public ApiSuccessResponse findOptionByOptionId(@PathVariable Long optionId) {
        ProductOptionResponseDto option = optionService.findOption(optionId);
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK", option);
    }

    // 5. 상품 옵션 조회 전체
    @GetMapping("/{productId}/all")
    public ApiSuccessResponse findAllOptionByProductId(@PathVariable Long productId) {
        List<ProductOptionResponseDto> options = optionService.findAllOptions(productId);
        return new ApiSuccessResponse(HttpStatus.OK.value(), "OK", options);
    }
}
