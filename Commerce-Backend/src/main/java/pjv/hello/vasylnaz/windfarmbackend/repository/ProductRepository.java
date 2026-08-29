package pjv.hello.vasylnaz.windfarmbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    Optional<Product> findByName(String name);

    List<Product> findByCategoriesIdAndAvailable(Long categoryId, boolean available);
}
