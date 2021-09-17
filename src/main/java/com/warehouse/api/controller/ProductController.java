package com.warehouse.api.controller;

import com.warehouse.api.resource.BuyProductsRequest;
import com.warehouse.api.resource.BuyProductsResponse;
import com.warehouse.api.resource.ProductApi;
import com.warehouse.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<ProductApi> getProducts() {
        return productService.getProducts();
    }

    @PostMapping("/products")
    public ProductApi addProduct(@RequestBody ProductApi product) {
        return productService.addProduct(product);
    }

    @GetMapping(value = "/product/{id}")
    public ProductApi getProductInformation(@PathVariable("id") long id) {
        return productService.getProduct(id);
    }

    @PostMapping(value = "/buyProduct")
    public BuyProductsResponse buyProductInformation(@RequestBody BuyProductsRequest buyProductsRequest) {
        return productService.buyProducts(buyProductsRequest);
    }

}
