package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateOrderRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Order;
import pjv.hello.vasylnaz.windfarmbackend.entity.OrderStatus;
import pjv.hello.vasylnaz.windfarmbackend.repository.AccountRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.OrderRepository;

import java.time.LocalDateTime;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;

    public OrderService(OrderRepository orderRepository, AccountRepository accountRepository) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        if(orderRepository.existsByAccountIdAndStatus(request.accountId(), OrderStatus.IN_PROGRESS)) {
            throw new RuntimeException("User already has an active order");
        }

        Order order = new Order();
        order.setCustomer(accountRepository.findById(request.accountId())
                .orElseThrow(() -> new RuntimeException("Account for order creation not found")));
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.IN_PROGRESS);

        return orderRepository.save(order);
    }
}
