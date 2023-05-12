package com.example.reactiveprogramming;

import com.example.reactiveprogramming.controller.ProductController;
import com.example.reactiveprogramming.dto.ProductDto;
import com.example.reactiveprogramming.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class ReactiveProgrammingApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService service;

    @Test
    public void addProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "mobile", 1, 10000));
        when(service.saveProduct(productDtoMono)).thenReturn(productDtoMono);
        webTestClient
                .post()
                .uri("/api/product")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getProductsTest() {
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("102", "mobile", 1, 10000),
                new ProductDto("103", "tv", 1, 50000));
        when(service.getProducts()).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient
                .get()
                .uri("/api/product")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("102", "mobile", 1, 10000))
                .expectNext(new ProductDto("103", "tv", 1, 50000))
                .verifyComplete();
    }

    @Test
    public void getProductByIdTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "mobile", 1, 10000));
        when(service.getProduct(any())).thenReturn(productDtoMono);

        Flux<ProductDto> responseBody = webTestClient
                .get()
                .uri("/api/product/102")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextMatches(p -> p.getName().equals("mobile"))
                .verifyComplete();
    }

    @Test
    public void updateProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "mobile", 1, 10000));
        when(service.updateProduct(productDtoMono, "102")).thenReturn(productDtoMono);
        webTestClient
                .put()
                .uri("/api/product/102")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void deleteProductTest() {
        BDDMockito.given(service.deleteProduct(any())).willReturn(Mono.empty());
        webTestClient
                .delete()
                .uri("/api/product/102")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
