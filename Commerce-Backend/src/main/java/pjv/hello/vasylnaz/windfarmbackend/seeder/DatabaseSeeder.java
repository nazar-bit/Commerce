package pjv.hello.vasylnaz.windfarmbackend.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.db.seed.enabled", havingValue = "true")
public class DatabaseSeeder implements CommandLineRunner {

    private final CategorySeeder categorySeeder;
    private final ProductSeeder productSeeder;
    private final ProductInstanceSeeder productInstanceSeeder;
    private final AccountSeeder accountSeeder;

    public DatabaseSeeder(CategorySeeder categorySeeder, ProductSeeder productSeeder,
                          ProductInstanceSeeder productInstanceSeeder, AccountSeeder accountSeeder) {
        this.categorySeeder = categorySeeder;
        this.productSeeder = productSeeder;
        this.productInstanceSeeder = productInstanceSeeder;
        this.accountSeeder = accountSeeder;
    }

    @Override
    public void run(String... args) {
        categorySeeder.seed();           // 1. Categories
        productSeeder.seed();            // 2. Products
        productInstanceSeeder.seed();    // 3. Product Instances
        accountSeeder.seed();            // 4. Accounts (for testing)
    }
}