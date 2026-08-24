package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.CancelOrderRequest;
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
    private final OrderItemService orderItemService;

    public OrderService(OrderRepository orderRepository,
                        AccountRepository accountRepository,
                        OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.orderItemService = orderItemService;
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

    @Transactional
    public Order cancelOrder(CancelOrderRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(!order.getStatus().equals(OrderStatus.IN_PROGRESS)) {
            throw new RuntimeException("Order is not in progress");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderItemService.releaseInstances(order.getId());

        return orderRepository.save(order);
    }
}
