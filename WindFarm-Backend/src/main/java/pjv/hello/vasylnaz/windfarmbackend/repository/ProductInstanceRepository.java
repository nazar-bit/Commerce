package pjv.hello.vasylnaz.windfarmbackend.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import pjv.hello.vasylnaz.windfarmbackend.entity.InstanceStatus;
import pjv.hello.vasylnaz.windfarmbackend.entity.ProductInstance;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductInstanceRepository extends JpaRepository<ProductInstance, Long> {
    List<ProductInstance> findByProductName(String name);
    Long countByProductIdAndStatus(Long productId, InstanceStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ProductInstance> findByProductIdAndStatus(Long productId, InstanceStatus status, Pageable pageable);
}
