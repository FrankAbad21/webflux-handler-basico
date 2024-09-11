package com.frab.spring.webflux.service;

import com.frab.spring.webflux.dto.ProductDto;
import com.frab.spring.webflux.entity.Product;
import com.frab.spring.webflux.exception.CustomException;
import com.frab.spring.webflux.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final static String NF_MESSAGE = "Product no found";
    private final static String NAME_MESSAGE = "Product name already in use";

    private final ProductRepository productRepository;

    public Flux<Product> getAll() {
        return productRepository.findAll();
    }

    public Mono<Product> getById(int id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }

    public Mono<Product> save(ProductDto productDto) {
        Mono<Boolean> existsName = productRepository.findByName(productDto.getName()).hasElement();
        //return productRepository.save(product);
        return existsName.flatMap(exist -> exist ? Mono.error(
                new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE)) :
                productRepository.save(
                        Product.builder()
                        .name(productDto.getName())
                        .price(productDto.getPrice()).build()));
    }

    public Mono<Product> update(ProductDto productDto, int id) {
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        Mono<Boolean> productRepeatedName = productRepository.repeatedName(id, productDto.getName())
                .hasElement();
        return productId.flatMap(
                existsId -> existsId ?
                        productRepeatedName.flatMap(existsName -> existsName ?
                                Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE))
                                : productRepository.save(new Product(id, productDto.getName(),
                                productDto.getPrice()))
                        )
                        : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE))
            );
    }

    public Mono<Void> delete(int id) {
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        return productId.flatMap(exists -> exists ?
                        productRepository.deleteById(id)
                : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE))
                );
    }

}
