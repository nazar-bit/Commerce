package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductInstanceRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductInstanceRepository productInstanceRepository;


    @Test
    void addProduct_ShouldSaveToDatabase() {
        CreateProductRequest request = new CreateProductRequest(
                "Test Laptop",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00
        );

        productService.addProduct(request);

        Product savedProduct = productRepository.findByName("Test Laptop")
                        .orElseThrow(()->new RuntimeException("Product not found"));

        assertEquals("Test Laptop", savedProduct.getName());
        assertEquals("A powerful testing machine", savedProduct.getDescription());
        assertEquals("http://www.example.com", savedProduct.getImageUrl());
        assertEquals(1200.00, savedProduct.getPrice());
    }


    @Test
    void addTwoProductsWithTheSameName(){
        CreateProductRequest request1 = new CreateProductRequest(
                "Test Laptop",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00
        );
        CreateProductRequest request2 = new CreateProductRequest(
                "Test Laptop",
                "A powerful machine",
                "http://www.example2.com",
                1300.00
        );

        productService.addProduct(request1);
        assertThrows(RuntimeException.class, () -> {
            productService.addProduct(request2);
        });
    }


    @Test
    void createTenProductInstances(){
        CreateProductRequest request1 = new CreateProductRequest(
                "Test Laptop",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00
        );

        productService.addProduct(request1);

        CreateProductInstancesRequest request2 = new CreateProductInstancesRequest(
                "Test Laptop",
                10
        );

        productService.createNewProductInstances(request2);
        assertEquals(10, productInstanceRepository.findByProductName("Test Laptop").size());
    }
}