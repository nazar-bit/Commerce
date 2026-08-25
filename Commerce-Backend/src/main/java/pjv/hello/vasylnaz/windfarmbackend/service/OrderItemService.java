package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderItemResponse;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderItemsRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.UnOrderItemsRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.InstanceStatus;
import pjv.hello.vasylnaz.windfarmbackend.entity.Order;
import pjv.hello.vasylnaz.windfarmbackend.entity.OrderItem;
import pjv.hello.vasylnaz.windfarmbackend.entity.ProductInstance;
import pjv.hello.vasylnaz.windfarmbackend.repository.OrderItemRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.OrderRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ProductInstanceService productInstanceService;

    public OrderItemService(OrderItemRepository orderItemRepository,
                            ProductRepository productRepository,
                            OrderRepository orderRepository,
                            ProductInstanceService productInstanceService) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.productInstanceService = productInstanceService;
    }

    @Transactional
    public List<OrderItem> orderItems(OrderItemsRequest request){
        double price = productRepository.findById(request.productId())
            .orElseThrow(() -> new RuntimeException("Product not found"))
            .getPrice();

        Order order = orderRepository.findById(request.orderId())
            .orElseThrow(() -> new RuntimeException("Order not found"));

        List<ProductInstance> instances =
            productInstanceService.reserveProductInstances(request.productId(), request.quantity());

        List<OrderItem> items = new ArrayList<>();
        for(ProductInstance instance : instances) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductInstance(instance);
            orderItem.setPriceAtPurchase(price);
            items.add(orderItem);
        }

        return orderItemRepository.saveAll(items);
    }


    @Transactional
    public void unOrderItems(UnOrderItemsRequest request){
        productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> items = orderItemRepository
                .findByOrderIdAndProductInstanceProductId(
                        request.orderId(),
                        request.productId(),
                        PageRequest.of(0, request.quantity()));

        for(OrderItem item : items) {
            item.getProductInstance().setStatus(InstanceStatus.AVAILABLE);
        }

        orderItemRepository.deleteAll(items);
    }


    @Transactional
    public void releaseInstances(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        for(OrderItem item : items) {
            item.getProductInstance().setStatus(InstanceStatus.AVAILABLE);
        }
    }


    @Transactional
    public void sellInstances(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        for(OrderItem item : items) {
            item.getProductInstance().setStatus(InstanceStatus.SOLD);
        }
    }


    public OrderItemResponse findById(Long id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Item not found"));
        return mapToResponse(item);
    }


    public OrderItemResponse mapToResponse(OrderItem orderItem) {
        return new OrderItemResponse(
            orderItem.getId(),
            orderItem.getProductInstance().getId(),
            orderItem.getOrder().getId(),
            orderItem.getPriceAtPurchase()
        );
    }
}
