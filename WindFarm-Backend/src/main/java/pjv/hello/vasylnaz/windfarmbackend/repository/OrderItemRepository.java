package pjv.hello.vasylnaz.windfarmbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjv.hello.vasylnaz.windfarmbackend.entity.OrderItem;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
