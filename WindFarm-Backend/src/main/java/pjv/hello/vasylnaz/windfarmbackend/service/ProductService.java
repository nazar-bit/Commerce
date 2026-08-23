package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductInstanceService productInstanceService;

    public ProductService(ProductRepository productRepository, ProductInstanceService productInstanceService) {
        this.productRepository = productRepository;
        this.productInstanceService = productInstanceService;
    }

    @Transactional
    public void addProduct(CreateProductRequest request) {
        if (productRepository.existsByName(request.name())) {
            throw new RuntimeException("Product name already exists");
        }

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImageUrl(request.imageUrl());

        productRepository.save(product);
    }

    @Transactional
    public void createNewProductInstances(CreateProductInstancesRequest request) {
        Product savedProduct = productRepository.findByName(request.productName())
                .orElseThrow(() -> new RuntimeException("Product with this name does not exist\n." +
                        " Cannot create product instances"));
        productInstanceService.createNewInstances(savedProduct, request.quantity());
    }
}
