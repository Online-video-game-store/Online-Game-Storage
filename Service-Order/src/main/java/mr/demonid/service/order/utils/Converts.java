package mr.demonid.service.order.utils;

import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.dto.OrderResponse;
import mr.demonid.service.order.dto.events.CatalogReserveRequestEvent;

import java.util.UUID;

public class Converts {

    public static CatalogReserveRequestEvent makeOrderCreatedEvent(UUID orderId, OrderCreateRequest request) {
        return new CatalogReserveRequestEvent(
                orderId,
                request.getUserId(),
                request.getPaymentId(),
                request.getCardId(),
                request.getTotalAmount(),
                request.getItems().stream().map(e -> e).toList()
        );
    }

    public static OrderResponse orderToDto(Order order) {
        return  new OrderResponse(
                order.getOrderId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getPaymentId(),
                order.getCreateAt(),
                order.getStatus()
        );
    }

    public static Order dtoToOrder(OrderResponse dto) {
        return Order.builder()
                .orderId(dto.getOrderId())
                .userId(dto.getUserId())
                .paymentId(dto.getPaymentId())
                .createAt(dto.getCreateAt())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .build();
    }

}
