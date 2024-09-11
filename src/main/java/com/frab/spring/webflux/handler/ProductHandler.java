package com.frab.spring.webflux.handler;

import com.frab.spring.webflux.dto.ProductDto;
import com.frab.spring.webflux.entity.Product;
import com.frab.spring.webflux.service.ProductService;
import com.frab.spring.webflux.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.management.monitor.MonitorNotification;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductService productService;

    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Product> products = productService.getAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(products, Product.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        int id = Integer.valueOf(request.pathVariable("id"));
        Mono<Product> product = productService.getById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(product, Product.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<ProductDto> productDto = request.bodyToMono(ProductDto.class).doOnNext(objectValidator::validate);
        return productDto.flatMap((prod -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.save(prod), Product.class)));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        int id = Integer.valueOf(request.pathVariable("id"));
        Mono<ProductDto> productDto = request.bodyToMono(ProductDto.class).doOnNext(objectValidator::validate);
        return productDto.flatMap((prod -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.update(prod, id), Product.class)));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        int id = Integer.valueOf(request.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.delete(id), Product.class);
    }

}
