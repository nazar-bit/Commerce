package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateOrderRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderItemsRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.*;
import pjv.hello.vasylnaz.windfarmbackend.repository.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
public class OrderItemServiceTest {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductInstanceRepository productInstanceRepository;


    @Test
    public void orderOneItemWithCorrectProductAndCorrectOrder() {
        // Create new Product
        CreateProductRequest requestProd = new CreateProductRequest(
                "Test",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                null
        );
        Product product = productService.addProduct(requestProd);
        // Create 12 instances
        productService.createNewProductInstances(new CreateProductInstancesRequest(product.getId(), 12));

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

        // Add one instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                order.getId(),
                product.getId(),
                1
        );

        orderItemService.orderItems(orderItemsRequest);

        // Assert that OrderItem was created
        assertEquals(1, orderItemRepository.count());
        // Assert that Reserved status was added
        assertEquals(1, productInstanceRepository.countByProductNameAndStatus("Test", InstanceStatus.RESERVED));
    }


    @Test
    public void orderOneItemWithCorrectProductAndInCorrectOrder() {
        // Create new Product
        CreateProductRequest requestProd = new CreateProductRequest(
                "Test",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                null
        );
        Product product = productService.addProduct(requestProd);
        // Create 12 instances
        productService.createNewProductInstances(new CreateProductInstancesRequest(product.getId(), 12));

        // Add one instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                -10000L,
                product.getId(),
                1
        );

        assertThrows(RuntimeException.class, () -> orderItemService.orderItems(orderItemsRequest));
    }


    @Test
    public void orderOneItemWithInCorrectProductAndCorrectOrder() {
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

        // Add one instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                order.getId(),
                -10000L,
                1
        );

        assertThrows(RuntimeException.class, () -> orderItemService.orderItems(orderItemsRequest));
    }


    @Test
    public void orderTenItemWithCorrectProductAndCorrectOrder() {
        // Create new Product
        CreateProductRequest requestProd = new CreateProductRequest(
                "Test",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                null
        );
        Product product = productService.addProduct(requestProd);
        // Create 12 instances
        productService.createNewProductInstances(new CreateProductInstancesRequest(product.getId(), 12));

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

        // Add one instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                order.getId(),
                product.getId(),
                10
        );

        orderItemService.orderItems(orderItemsRequest);

        // Assert that OrderItem was created
        assertEquals(10, orderItemRepository.count());
        // Assert that Reserved status was added
        assertEquals(10, productInstanceRepository.countByProductNameAndStatus("Test", InstanceStatus.RESERVED));
    }


    @Test
    public void orderTwentyItemWithCorrectProductAndCorrectOrder() {
        // Create new Product
        CreateProductRequest requestProd = new CreateProductRequest(
                "Test",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00,
                null
        );
        Product product = productService.addProduct(requestProd);
        // Create 12 instances
        productService.createNewProductInstances(new CreateProductInstancesRequest(product.getId(), 12));

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

        // Add one instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                order.getId(),
                product.getId(),
                20
        );


        assertThrows(RuntimeException.class, () -> orderItemService.orderItems(orderItemsRequest));
    }
}
