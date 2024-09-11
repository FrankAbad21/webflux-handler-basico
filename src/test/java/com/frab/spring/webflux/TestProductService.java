package com.frab.spring.webflux;

import com.frab.spring.webflux.dto.ProductDto;
import com.frab.spring.webflux.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.frab.spring.webflux.service.ProductService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class TestProductService {

    @Autowired
    ProductService ps;

    @Test
    void testSave() {
        Mono<Product> mockProduct = Mono.just(new Product(5,"product 5",50));
        Mono<Product> prod = ps.save(new ProductDto("product 5", 50));
        //StepVerifier.create(mockProduct).expectNext(prod).verifyComplete();
    }
}
