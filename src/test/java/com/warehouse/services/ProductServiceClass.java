package com.warehouse.services;

import com.warehouse.api.resource.ProductApi;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.entity.Product;
import com.warehouse.sampleDataForTests.SampleProductData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class ProductServiceClass {

    @TestConfiguration
    static class ProductServiceImplTestContextConfiguration {

        @Bean
        public ProductService clientService() {
            return new ProductService();
        }
    }

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    private SampleProductData sampleProductData = new SampleProductData();

    @Before
    public void setUp() {

        Mockito.when(productRepository.findById((long) 1))
                .thenReturn(Optional.of(sampleProductData.getTestProduct()));

        List<Product> productList = new ArrayList<>();

        productList.add(sampleProductData.getTestProduct());
        productList.add(sampleProductData.getTestProduct());

        Mockito.when(productRepository.findAll())
                .thenReturn(productList);
    }

    @Test
    public void getOneClient() {
        ProductApi found = productService.getProduct(1);

        assertThat(found.getName())
                .isEqualTo("sampleName");
    }

    @Test
    public void getAllClients() {
        List<ProductApi> found = productService.getProducts();

        assertEquals(found.size(), 2);
        assertThat(found.get(0).getName())
                .isEqualTo("sampleName");
        assertThat(found.get(1).getName())
                .isEqualTo("sampleName");
    }

    @Test
    public void addClient() {
        ProductApi productApi = productService.addProduct(sampleProductData.getTestProductApi());

        assertEquals(productApi.getName(), "sampleName");
        assertEquals(productApi.getCategory(), "sampleCategory");
        assertEquals(productApi.getDescription(), "sampleDescription");
        assertEquals(productApi.getUnitPrice().intValue(), 10);
        assertEquals(Optional.of(productApi.getUnitsInOrder()).get(), Optional.of(20L).get());
        assertEquals(Optional.of(productApi.getUnitsInStock()).get(), Optional.of(50L).get());
        assertEquals(Optional.of(productApi.getUnitsInStock()).get(), Optional.of(50L).get());
    }

}
