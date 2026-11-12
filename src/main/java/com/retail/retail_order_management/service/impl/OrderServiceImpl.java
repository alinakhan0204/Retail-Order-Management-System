
package com.retail.retail_order_management.service.impl;

import com.retail.retail_order_management.dto.request.OrderRequest;
import com.retail.retail_order_management.dto.response.OrderResponse;
import com.retail.retail_order_management.entity.Customer;
import com.retail.retail_order_management.entity.Order;
import com.retail.retail_order_management.entity.OrderStatus;
import com.retail.retail_order_management.exception.CustomerNotFoundException;
import com.retail.retail_order_management.repository.CustomerRepository;
import com.retail.retail_order_management.repository.OrderRepository;
import com.retail.retail_order_management.service.OrderService;
import com.retail.retail_order_management.exception.OrderNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CustomerRepository customerRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public OrderResponse createOrder(OrderRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: " + request.getCustomerId()));

        Order order = new Order();

        order.setOrderNumber(generateOrderNumber());
        order.setCustomer(customer);
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(request.getStatus() != null
                ? request.getStatus()
                : OrderStatus.PLACED);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with ID: " + id));

        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(
                    "Customer not found with ID: " + customerId);
        }

        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with ID: " + id));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: " + request.getCustomerId()));

        order.setCustomer(customer);
        order.setTotalAmount(request.getTotalAmount());

        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }

        return mapToResponse(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with ID: " + id));

        orderRepository.delete(order);
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private OrderResponse mapToResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setCustomerId(order.getCustomer().getId());

        response.setCustomerName(
                order.getCustomer().getFirstName() + " "
                + order.getCustomer().getLastName());

        return response;
    }
}
