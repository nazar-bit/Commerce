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

import java.awt.print.Pageable;
import java.util.List;


public interface ProductInstanceRepository extends JpaRepository<ProductInstance, Long> {
    List<ProductInstance> findByProductName(String name);
    Long countByProductIdAndStatus(Long productId, InstanceStatus status);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")})
//    @Query("SELECT i FROM ProductInstance i WHERE i.product.id = :productId AND i.status = 'AVAILABLE'")
//    List<ProductInstance> findAvailableInstancesAndLock(
//            @Param("productId") Long productId,
//            Pageable pageable
//    );
}
