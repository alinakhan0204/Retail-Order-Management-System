
package com.retail.retail_order_management.service;

import com.retail.retail_order_management.dto.request.OrderRequest;
import com.retail.retail_order_management.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getOrdersByCustomerId(Long customerId);

    OrderResponse updateOrder(Long id, OrderRequest request);

    void deleteOrder(Long id);
}
