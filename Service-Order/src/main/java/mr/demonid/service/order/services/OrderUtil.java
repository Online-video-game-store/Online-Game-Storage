package mr.demonid.service.order.services;


import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.dto.OrderResponse;

public class OrderUtil {

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
