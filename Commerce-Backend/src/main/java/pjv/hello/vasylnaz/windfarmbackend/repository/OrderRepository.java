package pjv.hello.vasylnaz.windfarmbackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pjv.hello.vasylnaz.windfarmbackend.entity.Order;
import pjv.hello.vasylnaz.windfarmbackend.entity.OrderStatus;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByAccountIdAndStatus(Long accountId, OrderStatus status);
    boolean existsByAccountIdAndStatus(Long accountId, OrderStatus status);

    List<Order> findOrderByAccountId(Long accountId);
}
