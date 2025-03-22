package mr.demonid.service.order.utils;

import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.dto.OrderCreatedEvent;

import java.util.UUID;

public class Converts {

    public static OrderCreatedEvent makeOrderCreatedEvent(UUID orderId, OrderCreateRequest request) {
        return new OrderCreatedEvent(
                orderId,
                request.getUserId(),
                request.getPaymentId(),
                request.getTotalAmount(),
                request.getItems().stream().map(e -> e).toList()
        );
    }

}
