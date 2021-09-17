package com.warehouse.controller;

import com.warehouse.api.resource.BuyProductsRequest;
import com.warehouse.api.resource.ProductApi;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.entity.Product;
import com.warehouse.sampleDataForTests.SampleProductData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductControllerTest {

    private static final String HTTP_LOCALHOST = "http://localhost:";

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductRepository productRepository;

    private SampleProductData sampleProductData = new SampleProductData();

    @Test
    public void getProduct_NoResults() throws Exception {
        mockMvc.perform(get("/product", 1)
                .param("id", "1"))
                .andExpect(status().is4xxClientError());

        assertThat(this.testRestTemplate.getForObject(HTTP_LOCALHOST + port + "/product/?id=1", String.class)).isNotNull();
    }

    @Test
    public void getProduct() {
        Product product = sampleProductData.getTestProduct();

        product = productRepository.save(product);

        ProductApi productApi = testRestTemplate.getForObject(HTTP_LOCALHOST + port + "/product/".concat(product.getId().toString()), ProductApi.class);

        assertEquals(productApi.getName(), "sampleName");
        assertEquals(productApi.getCategory(), "sampleCategory");
        assertEquals(productApi.getDescription(), "sampleDescription");
        assertEquals(productApi.getUnitPrice().intValue(), 10);
        assertEquals(Optional.of(productApi.getUnitsInOrder()).get(), Optional.of(20L).get());
        assertEquals(Optional.of(productApi.getUnitsInStock()).get(), Optional.of(50L).get());
    }

    @Test
    public void getProducts() {
        // act
        Product product1 = productRepository.save(sampleProductData.getTestProduct());
        Product product2 = productRepository.save(sampleProductData.getTestProduct());
        ResponseEntity<List<ProductApi>> clientsFromDb = testRestTemplate.exchange(HTTP_LOCALHOST + port + "/products", HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductApi>>() {
        });

        // assert
        assertThat(clientsFromDb.getBody().size() > 0);

        List<ProductApi> productApiList = clientsFromDb.getBody();
        ProductApi productApi = productApiList.stream()
                .filter(prod -> prod.getId() == product1.getId())
                .findFirst()
                .orElse(null);

        assertEquals(productApi.getName(), "sampleName");
        assertEquals(productApi.getCategory(), "sampleCategory");
        assertEquals(productApi.getDescription(), "sampleDescription");
        assertEquals(productApi.getUnitPrice().intValue(), 10);
        assertEquals(Optional.of(productApi.getUnitsInOrder()).get(), Optional.of(20L).get());
        assertEquals(Optional.of(productApi.getUnitsInStock()).get(), Optional.of(50L).get());

        ProductApi secondProduct = productApiList.stream()
                .filter(prod -> prod.getId() == product2.getId())
                .findFirst()
                .orElse(null);

        assertEquals(secondProduct.getName(), "sampleName");
        assertEquals(secondProduct.getCategory(), "sampleCategory");
        assertEquals(secondProduct.getDescription(), "sampleDescription");
        assertEquals(secondProduct.getUnitPrice().intValue(), 10);
        assertEquals(Optional.of(secondProduct.getUnitsInOrder()).get(), Optional.of(20L).get());
        assertEquals(Optional.of(secondProduct.getUnitsInStock()).get(), Optional.of(50L).get());
    }

    @Test
    public void addProduct() {
        // act
        ProductApi productApi = sampleProductData.getTestProductApi();
        ProductApi addedProduct = testRestTemplate.postForObject(HTTP_LOCALHOST + port + "/products", productApi, ProductApi.class);

        Optional<Product> productFromDb = productRepository.findById(addedProduct.getId());

        Product product = productFromDb.get();
        // assert
        assertEquals(product.getName(), "sampleName");
        assertEquals(product.getCategory(), "sampleCategory");
        assertEquals(product.getDescription(), "sampleDescription");
        assertEquals(product.getUnitPrice().intValue(), 10);
        assertEquals(Optional.of(product.getUnitsInOrder()).get(), Optional.of(20L).get());
        assertEquals(Optional.of(product.getUnitsInStock()).get(), Optional.of(50L).get());
    }

    @Test
    public void buyProduct() throws Exception {
        // act
        ProductApi productApi = sampleProductData.getTestProductApi();
        productApi.setUnitsInStock(1L);
        productApi.setUnitsInOrder(0L);
        ProductApi addedProduct = testRestTemplate.postForObject(HTTP_LOCALHOST + port + "/products", productApi, ProductApi.class);

        Optional<Product> productFromDb = productRepository.findById(addedProduct.getId());

        Product product = productFromDb.get();
        // assert
        assertEquals(product.getName(), "sampleName");
        assertEquals(product.getCategory(), "sampleCategory");
        assertEquals(product.getDescription(), "sampleDescription");
        assertEquals(product.getUnitPrice().intValue(), 10);
        assertEquals(Optional.of(product.getUnitsInOrder()).get(), Optional.of(0L).get());
        assertEquals(Optional.of(product.getUnitsInStock()).get(), Optional.of(1L).get());
        assertEquals(product.isSoldOut(), false);

        BuyProductsRequest buyProductsRequest = new BuyProductsRequest(new ArrayList<>(Arrays.asList(product.getId())));

        testRestTemplate.postForObject(HTTP_LOCALHOST + port + "/buyProduct", buyProductsRequest, BuyProductsRequest.class);

        Optional<Product> productBought = productRepository.findById(addedProduct.getId());

        Product productSoldOut = productBought.get();
        // assert
        assertEquals(productSoldOut.getId(), product.getId());
        assertEquals(productSoldOut.getName(), "sampleName");
        assertEquals(productSoldOut.getCategory(), "sampleCategory");
        assertEquals(productSoldOut.getDescription(), "sampleDescription");
        assertEquals(productSoldOut.getUnitPrice().intValue(), 10);
        assertEquals(Optional.of(productSoldOut.getUnitsInOrder()).get(), Optional.of(1L).get());
        assertEquals(Optional.of(productSoldOut.getUnitsInStock()).get(), Optional.of(0L).get());
        assertEquals(productSoldOut.isSoldOut(), true);

        mockMvc.perform(get("/buyProduct", productSoldOut.getId())
                .param("id", productSoldOut.getId().toString()))
                .andExpect(status().is4xxClientError());
    }

}

