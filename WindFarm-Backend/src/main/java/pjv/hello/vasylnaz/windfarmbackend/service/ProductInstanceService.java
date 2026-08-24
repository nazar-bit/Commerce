package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.InstanceStatus;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.entity.ProductInstance;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductInstanceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductInstanceService {

    private final ProductInstanceRepository productInstanceRepository;

    public ProductInstanceService(ProductInstanceRepository productInstanceRepository) {
        this.productInstanceRepository = productInstanceRepository;
    }

    @Transactional
    public List<ProductInstance> createNewInstances(Product product, int quantity){
        List<ProductInstance> instances = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            ProductInstance instance = new ProductInstance();
            instance.setProduct(product);
            instance.setStatus(InstanceStatus.AVAILABLE);
            instances.add(instance);
        }

        return productInstanceRepository.saveAll(instances);
    }

    @Transactional
    public List<ProductInstance> reserveProductInstances(Long productId, int quantity){
        List<ProductInstance> instances = productInstanceRepository
                .findByProductIdAndStatus(productId, InstanceStatus.AVAILABLE, PageRequest.of(0, quantity));

        if(instances.size() < quantity){
            throw new RuntimeException("Not enough available stock to fulfill the request.");
        }

        for(ProductInstance instance : instances) {
            instance.setStatus(InstanceStatus.RESERVED);
        }

        return productInstanceRepository.saveAll(instances);
    }
}
