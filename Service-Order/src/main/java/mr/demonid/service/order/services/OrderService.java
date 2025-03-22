package mr.demonid.service.order.services;


import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.dto.OrderResponse;

public interface OrderService {

    public OrderResponse save(OrderCreateRequest order);
    public OrderResponse getOrder(String orderId);
    public void deleteOrder(String orderId);
    public OrderResponse updateOrder(String orderId, OrderStatus status);
}
