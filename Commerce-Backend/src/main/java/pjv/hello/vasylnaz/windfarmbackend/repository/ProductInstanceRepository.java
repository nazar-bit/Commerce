package pjv.hello.vasylnaz.windfarmbackend.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pjv.hello.vasylnaz.windfarmbackend.entity.InstanceStatus;
import pjv.hello.vasylnaz.windfarmbackend.entity.ProductInstance;

import java.util.List;


public interface ProductInstanceRepository extends JpaRepository<ProductInstance, Long> {
    List<ProductInstance> findByProductName(String name);
    Long countByProductIdAndStatus(Long productId, InstanceStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ProductInstance> findByProductIdAndStatus(Long productId, InstanceStatus status, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ProductInstance> findByProductIdAndStatus(Long productId, InstanceStatus status);

    Long countByProductNameAndStatus(String productName, InstanceStatus status);
    Long countByStatus(InstanceStatus status);
}
