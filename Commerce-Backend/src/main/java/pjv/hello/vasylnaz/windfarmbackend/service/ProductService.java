package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.AssignCategoryToProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.ProductResponse;
import pjv.hello.vasylnaz.windfarmbackend.entity.Category;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.entity.ProductInstance;
import pjv.hello.vasylnaz.windfarmbackend.repository.CategoryRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductInstanceService productInstanceService;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          ProductInstanceService productInstanceService,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productInstanceService = productInstanceService;
        this.categoryRepository = categoryRepository;
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

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            while (category != null && !product.getCategories().contains(category)) {
                product.addCategory(category);
                category = category.getSuperCategory();
            }
        }

        return productRepository.save(product);
    }


    @Transactional
    public Product assignCategory(AssignCategoryToProductRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        while (category != null && !product.getCategories().contains(category)) {
            product.addCategory(category);
            category = category.getSuperCategory();
        }
        return productRepository.save(product);
    }


    @Transactional
    public List<ProductInstance> createNewProductInstances(CreateProductInstancesRequest request) {
        Product savedProduct = productRepository.findByName(request.productName())
                .orElseThrow(() -> new RuntimeException("Product with this name does not exist\n." +
                        " Cannot create product instances"));
        return productInstanceService.createNewInstances(savedProduct, request.quantity());
    }


    public ProductResponse findProductById(long id) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Product with this id does not exist"));

        return mapToResponse(product);
    }


    public List<ProductResponse> getProductsByCategory(long id) {
        List<Product> products = productRepository.findByCategoriesId(id);
        return products.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    public ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice(),
                product.getCategories().stream().map(Category::getId).collect(Collectors.toList())
        );
    }
}
