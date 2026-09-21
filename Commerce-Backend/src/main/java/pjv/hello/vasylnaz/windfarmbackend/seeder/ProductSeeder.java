package pjv.hello.vasylnaz.windfarmbackend.seeder;

import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pjv.hello.vasylnaz.windfarmbackend.entity.Category;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.repository.CategoryRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductSeeder {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Faker faker = new Faker();

    public ProductSeeder(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void seed() {
        if (productRepository.count() == 0) {
            // Fetch only subcategories
            List<Category> allCategories = categoryRepository.findAll();
            List<Category> subCategories = allCategories.stream()
                    .filter(c -> c.getSuperCategory() != null)
                    .collect(Collectors.toList());

            List<Product> productsToSave = new ArrayList<>();

            for (Category subCategory : subCategories) {
                Category parentCategory = subCategory.getSuperCategory();

                // Generate 15 products for every dynamically generated subcategory
                for (int i = 0; i < 15; i++) {
                    Product product = new Product();

                    // Creates a meaningful name like: "Ergonomic Bronze Chair"
                    String baseProductName = faker.commerce().productName();
                    product.setName(baseProductName);

                    product.setPrice(faker.number().randomDouble(2, 5, 1500));
                    product.setDescription(faker.lorem().paragraph(2));
                    product.setAvailable(faker.bool().bool());

                    // Maintain the Many-to-Many logic
                    product.addCategory(subCategory);
                    product.addCategory(parentCategory);

                    productsToSave.add(product);
                }
            }

            productRepository.saveAll(productsToSave);
            System.out.println("Products dynamically seeded.");
        }
    }
}