package pjv.hello.vasylnaz.windfarmbackend.seeder;

import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pjv.hello.vasylnaz.windfarmbackend.entity.Category;
import pjv.hello.vasylnaz.windfarmbackend.repository.CategoryRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class CategorySeeder {

    private final CategoryRepository categoryRepository;
    private final Faker faker = new Faker();

    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void seed() {
        if (categoryRepository.count() == 0) {
            Set<String> parentNames = new HashSet<>();
            int attempts = 0;

            // Generate unique Super Categories (Departments)
            while (parentNames.size() < 100 && attempts < 100) {
                parentNames.add(faker.commerce().department());
                attempts++;
            }

            for (String parentName : parentNames) {
                // 1. Save Parent
                Category parent = new Category();
                parent.setName(parentName);
                categoryRepository.save(parent);

                // 2. Generate unique Subcategories per Parent
                int subCatCount = faker.number().numberBetween(3, 7);
                Set<String> subNames = new HashSet<>();
                attempts = 0;

                while (subNames.size() < subCatCount && attempts < 100) {
                    // Combines a material with the parent name to make it meaningful
                    // Example: "Plastic Electronics" or "Cotton Kids"
                    String subCategoryName = faker.commerce().material() + " " + parentName;
                    subNames.add(subCategoryName);
                    attempts++;
                }

                // 3. Save Children
                for (String subName : subNames) {
                    Category child = new Category();
                    child.setName(subName);
                    child.setSuperCategory(parent);
                    categoryRepository.save(child);
                }
            }
            System.out.println("Categories dynamically seeded.");
        }
    }
}