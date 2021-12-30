package com.warehouse.services;

import com.warehouse.api.mapping.ProductApiProductMapper;
import com.warehouse.api.resource.BuyProductsRequest;
import com.warehouse.api.resource.BuyProductsResponse;
import com.warehouse.api.resource.ProductApi;
import com.warehouse.api.resource.ProductState;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductApiProductMapper productApiProductMapper;

    public List<ProductApi> getProducts() {
        return productApiProductMapper.clientListToClientApiList((List) productRepository.findAll());
    }

    public ProductApi addProduct(ProductApi product) {
        Product productDto = productApiProductMapper.productApiToProductDto(product);
        productRepository.save(productDto);
        return productApiProductMapper.productDtoToProductApi(productDto);
    }

    public ProductApi getProduct(long id) {
        return productApiProductMapper.productDtoToProductApi(getProductDto(id));
    }

    public Product getProductDto(long id) {
        return  findById(id).get();
    }

    private Optional<Product> findById(long id) {
        return Optional.ofNullable(productRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("product id: " + id)));
    }

    public BuyProductsResponse buyProducts(BuyProductsRequest buyProductsRequest) {
       return BuyProductsResponse.builder()
                .products(buyProductsRequest.getProductsId().stream().map(this::buyProduct).collect(Collectors.toList()))
                .build();
    }

    private ProductApi buyProduct(Long id) {
        Product product = getProductDto(id);

        if (product.isSoldOut()) {
            return returnProductWithAlreadySoldOutState(product);
        }

        return sellProduct(product);
    }

    private ProductApi sellProduct(@NotNull Product product) {
        product.setUnitsInStock(product.getUnitsInStock() - 1);
        product.setUnitsInOrder(product.getUnitsInOrder() + 1);
        product.setSoldOut(isSoldOut(product));

        productRepository.save(product);

        return productApiProductMapper.productDtoToProductApi(product);
    }

    private ProductApi returnProductWithAlreadySoldOutState(Product product) {
        ProductApi productApi = productApiProductMapper.productDtoToProductApi(product);
        productApi.setState(ProductState.ALREADY_SOLD_OUT);
        return productApi;
    }

    private boolean isSoldOut(Product product) {
        return product.getUnitsInStock() == 0;
    }
}
