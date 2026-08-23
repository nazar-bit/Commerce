package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderItemsRequest;
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
    public void orderItems(OrderItemsRequest request){
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

        orderItemRepository.saveAll(items);
    }
}
