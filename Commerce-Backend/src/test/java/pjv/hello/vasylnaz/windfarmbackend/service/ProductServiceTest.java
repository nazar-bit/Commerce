package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pjv.hello.vasylnaz.windfarmbackend.dto.AssignCategoryToProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateCategoryRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Category;
import pjv.hello.vasylnaz.windfarmbackend.entity.InstanceStatus;
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
    @Autowired
    private CategoryService categoryService;


    @Test
    void addProduct_ShouldSaveToDatabase() {
        CreateProductRequest request = new CreateProductRequest(
                "Test Laptop",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                null
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
                1200.00,
                null
        );
        CreateProductRequest request2 = new CreateProductRequest(
                "Test Laptop",
                "A powerful machine",
                "http://www.example2.com",
                1300.00,
                null
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
                1200.00,
                null
        );

        Product product = productService.addProduct(request1);

        CreateProductInstancesRequest request2 = new CreateProductInstancesRequest(
                product.getId(),
                10
        );

        productService.createNewProductInstances(request2);
        assertEquals(10, productInstanceRepository.findByProductName("Test Laptop").size());
    }


    @Test
    void deleteProduct(){
        CreateProductRequest request1 = new CreateProductRequest(
                "Test Laptop",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                null
        );

        Product product = productService.addProduct(request1);

        CreateProductInstancesRequest request2 = new CreateProductInstancesRequest(
                product.getId(),
                10
        );

        productService.createNewProductInstances(request2);
        productService.deleteProduct(product.getId());

        assertEquals(10, productInstanceRepository.findByProductIdAndStatus(product.getId(), InstanceStatus.ARCHIVED).size());
    }


    @Test
    void unassignCategoryCorrect(){
        CreateCategoryRequest requestCategory1 = new CreateCategoryRequest(
            "A",
            null
        );
        CreateCategoryRequest requestCategory2 = new CreateCategoryRequest(
                "B",
                null
        );

        Category category1 = categoryService.addCategory(requestCategory1);
        Category category2 = categoryService.addCategory(requestCategory2);

        CreateProductRequest request1 = new CreateProductRequest(
                "Test Laptop",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                category1.getId()
        );

        Product product = productService.addProduct(request1);

        productService.assignCategory(new AssignCategoryToProductRequest(
            product.getId(),
            category2.getId()
        ));

        product = productService.unAssignCategory(new AssignCategoryToProductRequest(
            product.getId(),
            category1.getId()
        ));

        assertFalse(product.getCategories().contains(category1));
        assertTrue(product.getCategories().contains(category2));
    }


    @Test
    void unassignCategoryWrong(){
        CreateCategoryRequest requestCategory1 = new CreateCategoryRequest(
                "A",
                null
        );

        Category category1 = categoryService.addCategory(requestCategory1);

        CreateProductRequest request1 = new CreateProductRequest(
                "Test Laptop",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                category1.getId()
        );

        Product product = productService.addProduct(request1);

        assertThrows(RuntimeException.class, () -> {
            productService.unAssignCategory(new AssignCategoryToProductRequest(
                    product.getId(),
                    category1.getId()
            ));
        });
    }


    @Test
    void unassignCategoryWrong2(){
        CreateCategoryRequest requestCategory1 = new CreateCategoryRequest(
                "A",
                null
        );
        CreateCategoryRequest requestCategory2 = new CreateCategoryRequest(
                "B",
                "A"
        );

        Category category1 = categoryService.addCategory(requestCategory1);
        Category category2 = categoryService.addCategory(requestCategory2);

        CreateProductRequest request1 = new CreateProductRequest(
                "Test Laptop",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                category2.getId()
        );

        Product product = productService.addProduct(request1);

        assertThrows(RuntimeException.class, () -> {
            productService.unAssignCategory(new AssignCategoryToProductRequest(
                    product.getId(),
                    category1.getId()
            ));
        });
    }


    @Test
    void unassignCategoryCorrectMultiple(){
        CreateCategoryRequest requestCategory1 = new CreateCategoryRequest(
                "A",
                null
        );
        CreateCategoryRequest requestCategory2 = new CreateCategoryRequest(
                "B",
                "A"
        );
        CreateCategoryRequest requestCategory3 = new CreateCategoryRequest(
                "C",
                null
        );

        Category category1 = categoryService.addCategory(requestCategory1);
        Category category2 = categoryService.addCategory(requestCategory2);
        Category category3 = categoryService.addCategory(requestCategory3);

        CreateProductRequest request1 = new CreateProductRequest(
                "Test Laptop",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                category2.getId()
        );

        Product product = productService.addProduct(request1);

        product = productService.assignCategory(new AssignCategoryToProductRequest(
                product.getId(),
                category3.getId()
        ));

        product = productService.unAssignCategory(new AssignCategoryToProductRequest(
                product.getId(),
                category1.getId()
        ));

        assertFalse(product.getCategories().contains(category1));
        assertFalse(product.getCategories().contains(category2));
        assertTrue(product.getCategories().contains(category3));
    }
}