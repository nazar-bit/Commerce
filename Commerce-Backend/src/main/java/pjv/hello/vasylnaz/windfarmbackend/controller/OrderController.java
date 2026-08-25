package pjv.hello.vasylnaz.windfarmbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateOrderRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderResponse;
import pjv.hello.vasylnaz.windfarmbackend.entity.Order;
import pjv.hello.vasylnaz.windfarmbackend.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable long id) {
        OrderResponse order = orderService.findOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        Order order = orderService.createOrder(createOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.mapToResponse(order));
    }

    @PutMapping("/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.cancelOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(orderService.mapToResponse(order));
    }

    @PutMapping("/complete")
    public ResponseEntity<OrderResponse> completeOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.completeOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(orderService.mapToResponse(order));
    }

    @PutMapping("/refund")
    public ResponseEntity<OrderResponse> refundOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.refundOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(orderService.mapToResponse(order));
    }
}
