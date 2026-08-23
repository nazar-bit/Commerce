package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateOrderRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Account;
import pjv.hello.vasylnaz.windfarmbackend.entity.Order;
import pjv.hello.vasylnaz.windfarmbackend.entity.OrderStatus;
import pjv.hello.vasylnaz.windfarmbackend.entity.Role;
import pjv.hello.vasylnaz.windfarmbackend.repository.AccountRepository;
import pjv.hello.vasylnaz.windfarmbackend.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;


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
}
