package com.viniciusaugusto.orderapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Product not found with ID: " + id);
    }
}
