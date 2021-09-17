package com.warehouse.api.mapping;

import com.warehouse.api.resource.ProductApi;
import com.warehouse.repository.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductApiProductMapper {
    ProductApi productDtoToProductApi(Product source);
    Product productApiToProductDto(ProductApi source);
    List<ProductApi> clientListToClientApiList(List<Product> source);
}

