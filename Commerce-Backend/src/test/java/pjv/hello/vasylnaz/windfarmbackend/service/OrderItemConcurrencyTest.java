package pjv.hello.vasylnaz.windfarmbackend.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pjv.hello.vasylnaz.windfarmbackend.dto.*;
import pjv.hello.vasylnaz.windfarmbackend.entity.*;
import pjv.hello.vasylnaz.windfarmbackend.repository.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class OrderItemConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductInstanceRepository productInstanceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Test
    public void testConcurrentUnsucessfullOrderReservation() throws InterruptedException {
        // Create 1 product, but only 1 instance available
        Product product = productService.addProduct(new CreateProductRequest("Turbine", "Desc", "url", 1000.0, null));
        productService.createNewProductInstances(new CreateProductInstancesRequest("Turbine", 1));

        // Create 2 separate users and 2 separate active orders
        Account user1 = accountService.registerAccount(new RegisterRequest("u1@x.com", "pass",
                Role.CUSTOMER, "U", "1", "09"));
        Account user2 = accountService.registerAccount(new RegisterRequest("u2@x.com", "pass",
                Role.CUSTOMER, "U", "2", "08"));

        Order order1 = orderService.createOrder(new CreateOrderRequest(user1.getId()));
        Order order2 = orderService.createOrder(new CreateOrderRequest(user2.getId()));

        // Create identical requests trying to buy that 1 instance
        OrderItemsRequest req1 = new OrderItemsRequest(order1.getId(), product.getId(), 1);
        OrderItemsRequest req2 = new OrderItemsRequest(order2.getId(), product.getId(), 1);

        // Concurrency setup
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startingGun = new CountDownLatch(1);
        CountDownLatch finishLine = new CountDownLatch(threadCount);

        AtomicInteger successfulOrders = new AtomicInteger(0);
        AtomicInteger failedOrders = new AtomicInteger(0);

        // load threads
        Runnable task1 = () -> {
            try {
                startingGun.await();
                orderItemService.orderItems(req1);
                successfulOrders.incrementAndGet();
            } catch (Exception e) {
                failedOrders.incrementAndGet();
            } finally {
                finishLine.countDown();
            }
        };

        Runnable task2 = () -> {
            try {
                startingGun.await();
                orderItemService.orderItems(req2);
                successfulOrders.incrementAndGet();
            } catch (Exception e) {
                failedOrders.incrementAndGet();
            } finally {
                finishLine.countDown();
            }
        };

        executor.submit(task1);
        executor.submit(task2);

        startingGun.countDown();

        // Wait for both threads to finish before asserting
        finishLine.await();

        // Assert
        assertEquals(1, successfulOrders.get());
        assertEquals(1, failedOrders.get());

        // Only 1 OrderItem should exist in the database total
        assertEquals(1, orderItemRepository.count());

        // The single instance should be marked as RESERVED
        assertEquals(1, productInstanceRepository.countByProductNameAndStatus("Turbine", InstanceStatus.RESERVED));
    }


    @Test
    public void testConcurrentSucessfullOrderReservation() throws InterruptedException {
        // Create 1 product, and 2 instance available
        Product product = productService.addProduct(new CreateProductRequest("Turbine", "Desc", "url", 1000.0, null));
        productService.createNewProductInstances(new CreateProductInstancesRequest("Turbine", 2));

        // Create 2 separate users and 2 separate active orders
        Account user1 = accountService.registerAccount(new RegisterRequest("u1@x.com", "pass",
                Role.CUSTOMER, "U", "1", "09"));
        Account user2 = accountService.registerAccount(new RegisterRequest("u2@x.com", "pass",
                Role.CUSTOMER, "U", "2", "08"));

        Order order1 = orderService.createOrder(new CreateOrderRequest(user1.getId()));
        Order order2 = orderService.createOrder(new CreateOrderRequest(user2.getId()));

        // Create identical requests trying to buy 1 instance
        OrderItemsRequest req1 = new OrderItemsRequest(order1.getId(), product.getId(), 1);
        OrderItemsRequest req2 = new OrderItemsRequest(order2.getId(), product.getId(), 1);

        // Concurrency setup
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startingGun = new CountDownLatch(1);
        CountDownLatch finishLine = new CountDownLatch(threadCount);

        AtomicInteger successfulOrders = new AtomicInteger(0);
        AtomicInteger failedOrders = new AtomicInteger(0);

        // load threads
        Runnable task1 = () -> {
            try {
                startingGun.await();
                orderItemService.orderItems(req1);
                successfulOrders.incrementAndGet();
            } catch (Exception e) {
                failedOrders.incrementAndGet();
            } finally {
                finishLine.countDown();
            }
        };

        Runnable task2 = () -> {
            try {
                startingGun.await();
                orderItemService.orderItems(req2);
                successfulOrders.incrementAndGet();
            } catch (Exception e) {
                failedOrders.incrementAndGet();
            } finally {
                finishLine.countDown();
            }
        };

        executor.submit(task1);
        executor.submit(task2);

        startingGun.countDown();

        // Wait for both threads to finish before asserting
        finishLine.await();

        // Assert
        assertEquals(2, successfulOrders.get());
        assertEquals(0, failedOrders.get());

        // Only 2 OrderItem should exist in the database total
        assertEquals(2, orderItemRepository.count());

        // 2 instances should be marked as RESERVED
        assertEquals(2, productInstanceRepository.countByProductNameAndStatus("Turbine", InstanceStatus.RESERVED));
    }


    @Test
    public void testConcurrentSucessfullOrderReservationMultiple() throws InterruptedException {
        // Create 1 product, and 5 instances available
        Product product = productService.addProduct(new CreateProductRequest("Turbine", "Desc", "url", 1000.0, null));
        productService.createNewProductInstances(new CreateProductInstancesRequest("Turbine", 5));

        // Create 2 separate users and 2 separate active orders
        Account user1 = accountService.registerAccount(new RegisterRequest("u1@x.com", "pass",
                Role.CUSTOMER, "U", "1", "09"));
        Account user2 = accountService.registerAccount(new RegisterRequest("u2@x.com", "pass",
                Role.CUSTOMER, "U", "2", "08"));

        Order order1 = orderService.createOrder(new CreateOrderRequest(user1.getId()));
        Order order2 = orderService.createOrder(new CreateOrderRequest(user2.getId()));

        // Create identical requests trying to buy 4 instances each
        OrderItemsRequest req1 = new OrderItemsRequest(order1.getId(), product.getId(), 4);
        OrderItemsRequest req2 = new OrderItemsRequest(order2.getId(), product.getId(), 4);

        // Concurrency setup
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startingGun = new CountDownLatch(1);
        CountDownLatch finishLine = new CountDownLatch(threadCount);

        AtomicInteger successfulOrders = new AtomicInteger(0);
        AtomicInteger failedOrders = new AtomicInteger(0);

        // load threads
        Runnable task1 = () -> {
            try {
                startingGun.await();
                orderItemService.orderItems(req1);
                successfulOrders.incrementAndGet();
            } catch (Exception e) {
                System.err.println("Thread 1 Failed: " + e.getMessage());
                e.printStackTrace();
                failedOrders.incrementAndGet();
            } finally {
                finishLine.countDown();
            }
        };

        Runnable task2 = () -> {
            try {
                startingGun.await();
                orderItemService.orderItems(req2);
                successfulOrders.incrementAndGet();
            } catch (Exception e) {
                System.err.println("Thread 2 Failed: " + e.getMessage());
                e.printStackTrace();
                failedOrders.incrementAndGet();
            } finally {
                finishLine.countDown();
            }
        };

        executor.submit(task1);
        executor.submit(task2);

        startingGun.countDown();

        // Wait for both threads to finish before asserting
        finishLine.await();

        // Assert
        assertEquals(1, successfulOrders.get());
        assertEquals(1, failedOrders.get());

        // 4 OrderItems should exist in the database total
        assertEquals(4, orderItemRepository.count());

        // 4 instances should be marked as RESERVED
        assertEquals(4, productInstanceRepository.countByProductNameAndStatus("Turbine", InstanceStatus.RESERVED));
    }


    @AfterEach
    public void tearDown() {
        orderItemRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productInstanceRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }
}
