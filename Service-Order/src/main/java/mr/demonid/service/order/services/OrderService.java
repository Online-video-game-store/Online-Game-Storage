package mr.demonid.service.order.services;


import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.dto.OrderResponse;

import java.util.UUID;

public interface OrderService {

    void createOrder(OrderCreateRequest request);
    OrderResponse updateOrder(UUID orderId, OrderStatus status);

    OrderResponse getOrder(String orderId);
    void deleteOrder(String orderId);
}
