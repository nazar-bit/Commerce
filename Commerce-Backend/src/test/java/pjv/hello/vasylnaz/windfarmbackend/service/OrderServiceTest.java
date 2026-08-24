package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pjv.hello.vasylnaz.windfarmbackend.dto.*;
import pjv.hello.vasylnaz.windfarmbackend.entity.*;
import pjv.hello.vasylnaz.windfarmbackend.repository.AccountRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.OrderItemRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.OrderRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductInstanceRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductInstanceRepository productInstanceRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Test
    public void createOrderWithNonExistentAccount() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                -100000L
        );

        assertThrows(RuntimeException.class, () ->
            orderService.createOrder(createOrderRequest));
    }

    @Test
    public void createOrderWithValidAccount() {
        Account account = new Account();
        account.setEmail("test@gmail.com");
        account.setPassword("bad");
        account.setFirstName("Test");
        account.setLastName("Tester");
        account.setRole(Role.CUSTOMER);

        Account saved = accountRepository.save(account);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
            saved.getId()
        );

        orderService.createOrder(createOrderRequest);

        Order savedOrder = orderRepository.findOrderByAccountIdAndStatus(saved.getId(), OrderStatus.IN_PROGRESS)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }


    @Test
    public void createTwoActiveOrdersForSameAccount() {
        Account account = new Account();
        account.setEmail("test@gmail.com");
        account.setPassword("bad");
        account.setFirstName("Test");
        account.setLastName("Tester");
        account.setRole(Role.CUSTOMER);

        Account saved = accountRepository.save(account);

        CreateOrderRequest createOrderRequest1 = new CreateOrderRequest(
                saved.getId()
        );
        CreateOrderRequest createOrderRequest2 = new CreateOrderRequest(
                saved.getId()
        );

        orderService.createOrder(createOrderRequest1);
        assertThrows(RuntimeException.class, () ->
                orderService.createOrder(createOrderRequest2));
    }


    @Test
    public void cancelOrderTest() {
        // Create new Product
        CreateProductRequest requestProd = new CreateProductRequest(
                "Test",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00
        );
        Product product = productService.addProduct(requestProd);
        // Create 12 instances
        productService.createNewProductInstances(new CreateProductInstancesRequest("Test", 12));

        // Create new Account
        Account account = new Account();
        account.setEmail("test@gmail.com");
        account.setPassword("bad");
        account.setFirstName("Test");
        account.setLastName("Tester");
        account.setRole(Role.CUSTOMER);

        Account saved = accountRepository.save(account);

        // Create new empty order
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                saved.getId()
        );

        Order order = orderService.createOrder(createOrderRequest);

        // Add instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                order.getId(),
                product.getId(),
                10
        );

        orderItemService.orderItems(orderItemsRequest);

        // Cancel order
        OrderRequest orderRequest = new OrderRequest(order.getId());
        order = orderService.cancelOrder(orderRequest);

        // Assert
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(0, productInstanceRepository.countByStatus(InstanceStatus.RESERVED));
        assertEquals(10, orderItemRepository.count());
    }


    @Test
    public void cancelInvalidOrderTest() {
        assertThrows(RuntimeException.class,
                () -> orderService.cancelOrder(new OrderRequest(-1000L)));
    }


    @Test
    public void completeOrderTest() {
        // Create new Product
        CreateProductRequest requestProd = new CreateProductRequest(
                "Test",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00
        );
        Product product = productService.addProduct(requestProd);
        // Create 12 instances
        productService.createNewProductInstances(new CreateProductInstancesRequest("Test", 12));

        // Create new Account
        Account account = new Account();
        account.setEmail("test@gmail.com");
        account.setPassword("bad");
        account.setFirstName("Test");
        account.setLastName("Tester");
        account.setRole(Role.CUSTOMER);

        Account saved = accountRepository.save(account);

        // Create new empty order
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                saved.getId()
        );

        Order order = orderService.createOrder(createOrderRequest);

        // Add instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                order.getId(),
                product.getId(),
                10
        );

        orderItemService.orderItems(orderItemsRequest);

        // Complete order
        OrderRequest orderRequest = new OrderRequest(order.getId());
        order = orderService.completeOrder(orderRequest);

        // Assert
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        assertEquals(10, productInstanceRepository.countByStatus(InstanceStatus.SOLD));
        assertEquals(10, orderItemRepository.count());
    }


    @Test
    public void refundOrderTest() {
        // Create new Product
        CreateProductRequest requestProd = new CreateProductRequest(
                "Test",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00
        );
        Product product = productService.addProduct(requestProd);
        // Create 12 instances
        productService.createNewProductInstances(new CreateProductInstancesRequest("Test", 12));

        // Create new Account
        Account account = new Account();
        account.setEmail("test@gmail.com");
        account.setPassword("bad");
        account.setFirstName("Test");
        account.setLastName("Tester");
        account.setRole(Role.CUSTOMER);

        Account saved = accountRepository.save(account);

        // Create new empty order
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                saved.getId()
        );

        Order order = orderService.createOrder(createOrderRequest);

        // Add instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                order.getId(),
                product.getId(),
                10
        );

        orderItemService.orderItems(orderItemsRequest);

        // Complete order
        OrderRequest orderRequest = new OrderRequest(order.getId());
        order = orderService.completeOrder(orderRequest);

        // Refund Order
        order = orderService.refundOrder(orderRequest);

        // Assert
        assertEquals(OrderStatus.REFUNDED, order.getStatus());
        assertEquals(0, productInstanceRepository.countByStatus(InstanceStatus.SOLD));
        assertEquals(10, orderItemRepository.count());
    }


    @Test
    public void refundOrderTestAfter30Days() {
        // Create new Product
        CreateProductRequest requestProd = new CreateProductRequest(
                "Test",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00
        );
        Product product = productService.addProduct(requestProd);
        // Create 12 instances
        productService.createNewProductInstances(new CreateProductInstancesRequest("Test", 12));

        // Create new Account
        Account account = new Account();
        account.setEmail("test@gmail.com");
        account.setPassword("bad");
        account.setFirstName("Test");
        account.setLastName("Tester");
        account.setRole(Role.CUSTOMER);

        Account saved = accountRepository.save(account);

        // Create new empty order
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                saved.getId()
        );

        Order order = orderService.createOrder(createOrderRequest);

        // Add instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                order.getId(),
                product.getId(),
                10
        );

        orderItemService.orderItems(orderItemsRequest);

        // Complete order
        OrderRequest orderRequest = new OrderRequest(order.getId());
        order = orderService.completeOrder(orderRequest);
        // Subtract 31 days
        order.setResolvedAt(order.getResolvedAt().minusDays(31));
        orderRepository.save(order);

        // Refund Order
        assertThrows(RuntimeException.class, () -> orderService.refundOrder(orderRequest));
    }
}
