package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.entity.ProductInstance;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductInstanceService productInstanceService;

    public ProductService(ProductRepository productRepository, ProductInstanceService productInstanceService) {
        this.productRepository = productRepository;
        this.productInstanceService = productInstanceService;
    }

    @Transactional
    public Product addProduct(CreateProductRequest request) {
        if (productRepository.existsByName(request.name())) {
            throw new RuntimeException("Product name already exists");
        }

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImageUrl(request.imageUrl());

        return productRepository.save(product);
    }

    @Transactional
    public List<ProductInstance> createNewProductInstances(CreateProductInstancesRequest request) {
        Product savedProduct = productRepository.findByName(request.productName())
                .orElseThrow(() -> new RuntimeException("Product with this name does not exist\n." +
                        " Cannot create product instances"));
        return productInstanceService.createNewInstances(savedProduct, request.quantity());
    }
}
