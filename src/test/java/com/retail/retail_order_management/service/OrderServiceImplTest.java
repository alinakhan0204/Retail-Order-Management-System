
package com.retail.retail_order_management.service;

import com.retail.retail_order_management.dto.request.OrderRequest;
import com.retail.retail_order_management.dto.response.OrderResponse;
import com.retail.retail_order_management.entity.Customer;
import com.retail.retail_order_management.entity.Order;
import com.retail.retail_order_management.entity.OrderStatus;
import com.retail.retail_order_management.exception.CustomerNotFoundException;
import com.retail.retail_order_management.exception.OrderNotFoundException;
import com.retail.retail_order_management.repository.CustomerRepository;
import com.retail.retail_order_management.repository.OrderRepository;
import com.retail.retail_order_management.service.impl.OrderServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private Order order;
    private OrderRequest request;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Alina");
        customer.setLastName("Khan");
        customer.setEmail("alina@example.com");

        order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-12345678");
        order.setCustomer(customer);
        order.setTotalAmount(new BigDecimal("2499.99"));
        order.setStatus(OrderStatus.PLACED);

        request = new OrderRequest();
        request.setCustomerId(1L);
        request.setTotalAmount(new BigDecimal("2499.99"));
        request.setStatus(OrderStatus.PLACED);
    }

    @Test
    void createOrder_Success() {

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Alina Khan", response.getCustomerName());
        assertEquals(OrderStatus.PLACED, response.getStatus());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_CustomerNotFound() {

        when(customerRepository.findById(999L))
                .thenReturn(Optional.empty());

        request.setCustomerId(999L);

        assertThrows(
                CustomerNotFoundException.class,
                () -> orderService.createOrder(request)
        );

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrderById_Success() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResponse response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals("ORD-12345678", response.getOrderNumber());
        assertEquals(new BigDecimal("2499.99"), response.getTotalAmount());
    }

    @Test
    void getOrderById_NotFound() {

        when(orderRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(999L)
        );
    }

    @Test
    void getAllOrders_Success() {

        when(orderRepository.findAll())
                .thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getAllOrders();

        assertEquals(1, responses.size());
        assertEquals("ORD-12345678", responses.get(0).getOrderNumber());
    }

    @Test
    void getOrdersByCustomerId_Success() {

        when(customerRepository.existsById(1L))
                .thenReturn(true);

        when(orderRepository.findByCustomerId(1L))
                .thenReturn(List.of(order));

        List<OrderResponse> responses =
                orderService.getOrdersByCustomerId(1L);

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getCustomerId());
    }

    @Test
    void updateOrder_Success() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        request.setStatus(OrderStatus.PROCESSING);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        OrderResponse response = orderService.updateOrder(1L, request);

        assertNotNull(response);
        assertEquals(OrderStatus.PROCESSING, order.getStatus());

        verify(orderRepository).save(order);
    }

    @Test
    void deleteOrder_Success() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        orderService.deleteOrder(1L);

        verify(orderRepository).delete(order);
    }
}