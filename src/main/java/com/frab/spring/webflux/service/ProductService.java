package com.frab.spring.webflux.service;

import com.frab.spring.webflux.entity.Product;
import com.frab.spring.webflux.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Flux<Product> getAll() {
        return productRepository.findAll();
    }

    public Mono<Product> getById(int id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("Product no encontrado")));
    }

    public Mono<Product> save(Product product) {
        Mono<Boolean> existsName = productRepository.findByName(product.getName()).hasElement();
        return existsName.flatMap(exist -> exist ? Mono.error(new Exception("El nombre del producto ya existe")) :
                productRepository.save(product));
    }

    public Mono<Product> update(Product product, int id) {
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        Mono<Boolean> productRepeatedName = productRepository.repeatedName(id, product.getName())
                .hasElement();
        return productId.flatMap(
                existsId -> existsId ?
                        productRepeatedName.flatMap(existsName -> existsName ?
                                Mono.error(new Exception("El nombre ya existe"))
                                : productRepository.save(new Product(id, product.getName(),
                                product.getPrice()))
                        )
                        : Mono.error(new Exception("Product no found"))
            );
    }

    public Mono<Void> delete(int id) {
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        return productId.flatMap(exists -> exists ?
                        productRepository.deleteById(id)
                : Mono.error(new Exception("Product no found"))
                );
    }

}
