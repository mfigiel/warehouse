package com.warehouse.sampleDataForTests;

import com.warehouse.api.resource.ProductApi;
import com.warehouse.repository.entity.Product;
import java.math.BigDecimal;

public class SampleProductData {
    private static final String CATEGORY = "sampleCategory";
    private static final String DESCRIPTION = "sampleDescription";
    private static final String NAME = "sampleName";
    private static final int UNIT_PRICE = 10;
    private static final int UNITS_IN_ORDER = 20;
    private static final int UNITS_IN_STOCK = 50;

    public ProductApi getTestProductApi() {
        ProductApi productApi = new ProductApi();
        productApi.setCategory(CATEGORY);
        productApi.setDescription(DESCRIPTION);
        productApi.setName(NAME);
        productApi.setUnitPrice(new BigDecimal(UNIT_PRICE));
        productApi.setUnitsInOrder((long) UNITS_IN_ORDER);
        productApi.setUnitsInStock((long) 50);
        return productApi;
    }

    public Product getTestProduct() {
        Product product = new Product();
        product.setSoldOut(false);
        product.setCategory(CATEGORY);
        product.setDescription(DESCRIPTION);
        product.setName(NAME);
        product.setUnitPrice(new BigDecimal(UNIT_PRICE));
        product.setUnitsInOrder((long) UNITS_IN_ORDER);
        product.setUnitsInStock((long) UNITS_IN_STOCK);
        return product;
    }
}
