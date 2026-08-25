package pjv.hello.vasylnaz.windfarmbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.ProductResponse;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable long id) {
        ProductResponse product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }


    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        Product product = productService.addProduct(createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.mapToResponse(product));
    }


    @PostMapping("/create/instances")
    public ResponseEntity<Void> createProductInstances(@RequestBody CreateProductInstancesRequest createProductInstancesRequest) {
        productService.createNewProductInstances(createProductInstancesRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
