package pjv.hello.vasylnaz.windfarmbackend.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import pjv.hello.vasylnaz.windfarmbackend.entity.ProductInstance;

import java.util.List;


public interface ProductInstanceRepository extends JpaRepository<ProductInstance, Long> {
    List<ProductInstance> findByProductName(String name);
}
