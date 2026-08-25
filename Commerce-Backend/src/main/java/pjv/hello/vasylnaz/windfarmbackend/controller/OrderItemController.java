package pjv.hello.vasylnaz.windfarmbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderItemResponse;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderItemsRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.UnOrderItemsRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.OrderItem;
import pjv.hello.vasylnaz.windfarmbackend.service.OrderItemService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order_items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> findById(@PathVariable Long id) {
        OrderItemResponse orderItem = orderItemService.findById(id);
        return ResponseEntity.ok(orderItem);
    }

    @PostMapping("/order")
    public ResponseEntity<List<OrderItemResponse>> order(@RequestBody OrderItemsRequest orderItemsRequest) {
        List<OrderItem> orderItems = orderItemService.orderItems(orderItemsRequest);
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemResponses.add(orderItemService.mapToResponse(orderItem));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemResponses);
    }

    @DeleteMapping("/un_order")
    public ResponseEntity<Void> unOrder(@RequestBody UnOrderItemsRequest unOrderItemsRequest) {
        orderItemService.unOrderItems(unOrderItemsRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
