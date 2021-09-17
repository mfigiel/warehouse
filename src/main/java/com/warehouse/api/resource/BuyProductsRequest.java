package com.warehouse.api.resource;

import lombok.Data;

import java.util.List;

@Data
public class BuyProductsRequest {
    private List<Long> productsId;

    public BuyProductsRequest() {}
    public BuyProductsRequest(List<Long> products) {
        this.productsId = products;
    }
}
