package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.InstanceStatus;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.entity.ProductInstance;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductInstanceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductInstanceService {

    private final ProductInstanceRepository productInstanceRepository;

    public ProductInstanceService(ProductInstanceRepository productInstanceRepository) {
        this.productInstanceRepository = productInstanceRepository;
    }

    @Transactional
    public void createNewInstances(Product product, int quantity){
        List<ProductInstance> instances = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            ProductInstance instance = new ProductInstance();
            instance.setProduct(product);
            instance.setStatus(InstanceStatus.AVAILABLE);
            instances.add(instance);
        }

        productInstanceRepository.saveAll(instances);
    }
}
