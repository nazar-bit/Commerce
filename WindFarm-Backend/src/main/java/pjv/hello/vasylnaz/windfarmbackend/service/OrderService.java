package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderRequest;
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
    public Order cancelOrder(OrderRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(!order.getStatus().equals(OrderStatus.IN_PROGRESS)) {
            throw new RuntimeException("Order is not in progress");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setResolvedAt(LocalDateTime.now());
        orderItemService.releaseInstances(order.getId());

        return orderRepository.save(order);
    }

    @Transactional
    public Order completeOrder(OrderRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(!order.getStatus().equals(OrderStatus.IN_PROGRESS)) {
            throw new RuntimeException("Order is not in progress");
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setResolvedAt(LocalDateTime.now());
        orderItemService.sellInstances(order.getId());

        return orderRepository.save(order);
    }

    @Transactional
    public Order refundOrder(OrderRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(!order.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new RuntimeException("Cannot Refund. Order was not completed");
        }

        LocalDateTime purchaseDate = order.getResolvedAt();
        LocalDateTime refundDeadline = purchaseDate.plusDays(30);
        if (LocalDateTime.now().isAfter(refundDeadline)) {
            throw new RuntimeException("Refund period has expired. Purchases older than 30 days cannot be refunded.");
        }

        order.setStatus(OrderStatus.REFUNDED);
        order.setRefundedAt(LocalDateTime.now());
        orderItemService.releaseInstances(order.getId());

        return orderRepository.save(order);
    }
}
