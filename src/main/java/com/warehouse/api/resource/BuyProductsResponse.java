package com.warehouse.api.resource;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class BuyProductsResponse {
    List<ProductApi> products;
}
