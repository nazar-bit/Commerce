package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductInstanceRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ProductInstanceServiceTest {

    @Autowired
    private ProductInstanceService productInstanceService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductInstanceRepository productInstanceRepository;

    @Test
    public void addOneInstance() {
        Product product = new Product();
        product.setName("Test");
        product.setPrice(300.00);

        Product savedProduct = productRepository.save(product);

        productInstanceService.createNewInstances(savedProduct, 1);
        assertEquals(1, productInstanceRepository.findByProductName(savedProduct.getName()).size());
    }

    @Test
    public void addTenInstances() {
        Product product = new Product();
        product.setName("Test");
        product.setPrice(300.00);

        Product savedProduct = productRepository.save(product);

        productInstanceService.createNewInstances(savedProduct, 10);
        assertEquals(10, productInstanceRepository.findByProductName(savedProduct.getName()).size());
    }
}
