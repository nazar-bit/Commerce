package pjv.hello.vasylnaz.windfarmbackend.seeder;

import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pjv.hello.vasylnaz.windfarmbackend.entity.InstanceStatus;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.entity.ProductInstance;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductInstanceRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductInstanceSeeder {

    private final ProductInstanceRepository productInstanceRepository;
    private final ProductRepository productRepository;
    private final Faker faker = new Faker();

    public ProductInstanceSeeder(ProductInstanceRepository productInstanceRepository, ProductRepository productRepository) {
        this.productInstanceRepository = productInstanceRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void seed() {
        if (productInstanceRepository.count() == 0) {
            List<Product> products = productRepository.findAll();
            List<ProductInstance> instancesToSave = new ArrayList<>();

            // Retrieves all available enum values to pick from randomly
            InstanceStatus[] statuses = InstanceStatus.values();

            for (Product product : products) {
                int instanceCount = faker.number().numberBetween(10, 61);

                for (int i = 0; i < instanceCount; i++) {
                    ProductInstance instance = new ProductInstance();
                    instance.setProduct(product);

                    // Pick a random status from the enum
                    InstanceStatus randomStatus = statuses[faker.random().nextInt(statuses.length)];
                    instance.setStatus(randomStatus);

                    instancesToSave.add(instance);
                }
            }

            productInstanceRepository.saveAll(instancesToSave);
        }
    }
}