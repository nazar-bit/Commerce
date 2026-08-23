package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateOrderRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductInstancesRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateProductRequest;
import pjv.hello.vasylnaz.windfarmbackend.dto.OrderItemsRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Account;
import pjv.hello.vasylnaz.windfarmbackend.entity.OrderStatus;
import pjv.hello.vasylnaz.windfarmbackend.entity.Product;
import pjv.hello.vasylnaz.windfarmbackend.entity.Role;
import pjv.hello.vasylnaz.windfarmbackend.repository.AccountRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.OrderItemRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.OrderRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void orderOneItemWithCorrectProductAndCorrectOrder() {
        // Create new Product
        CreateProductRequest requestProd = new CreateProductRequest(
                "Test",
                "A powerful testing machine",
                "http://www.example.com",
                1200.00
        );
        productService.addProduct(requestProd);
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

        orderService.createOrder(createOrderRequest);

        // Add one instance to OrderItem
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(
                orderRepository.findOrderByAccountIdAndStatus(saved.getId(), OrderStatus.IN_PROGRESS).get().getId(),
                productRepository.findByName("Test").get().getId(),
                1
        );

        orderItemService.orderItems(orderItemsRequest);

        // Assert
        assertEquals(1, orderItemRepository.count());
    }
}
