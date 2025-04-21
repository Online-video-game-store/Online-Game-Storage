package mr.demonid.service.order.services;


import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.dto.OrderResponse;
import mr.demonid.service.order.dto.filters.OrderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    void createOrder(OrderCreateRequest request);
    OrderResponse updateOrder(UUID orderId, OrderStatus status);

    OrderResponse getOrder(String orderId);
    void deleteOrder(String orderId);

    Page<OrderResponse> getAllOrders(OrderFilter filter, Pageable pageable);

}
