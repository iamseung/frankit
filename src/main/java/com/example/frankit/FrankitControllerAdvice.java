package com.example.frankit;

import com.example.frankit.dto.response.product.ProductApiResponseDto;
import com.example.frankit.exception.ProductException;
import com.example.frankit.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FrankitControllerAdvice {

    @ExceptionHandler(ProductException.class)
    public ProductApiResponseDto productExceptionHandler(ProductException exception) {
        return new ProductApiResponseDto(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ProductApiResponseDto userExceptionHandler(UserException exception) {
        return new ProductApiResponseDto(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
