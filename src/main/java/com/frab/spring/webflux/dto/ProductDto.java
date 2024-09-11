package com.frab.spring.webflux.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDto {

    @NotBlank(message = "name is required")
    private String name;
    @Min(value = 1, message = "price must be greater then zero")
    private float price;

}
