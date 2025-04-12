package mr.demonid.service.order.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.dto.OrderResponse;
import mr.demonid.service.order.events.OrderPublisher;
import mr.demonid.service.order.exceptions.CreateOrderException;
import mr.demonid.service.order.repository.OrderRepository;
import mr.demonid.service.order.utils.Converts;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderPublisher orderPublisher;

    /**
     * Открывает новый заказ и запускает процесс его оформления.
     * Возвращает предварительное состояние операции, не дожидаясь её фактического завершения.
     */
    public void createOrder(OrderCreateRequest request) {

        try {
            Order order = Order.builder()
                    .userId(request.getUserId())
                    .paymentId(request.getPaymentId())
                    .cardId(request.getCardId())
                    .totalAmount(request.getTotalAmount())
                    .status(OrderStatus.Pending)
                    .build();
            Order res = orderRepository.save(order);
            orderPublisher.sendCreateOrderEvent(mr.demonid.service.order.utils.Converts.makeOrderCreatedEvent(res.getOrderId(), request));
        } catch (Exception e) {
            log.error("OrderServiceImpl.createOrder(): {}", e.getMessage());
            throw new CreateOrderException(e.getMessage());
        }
    }

    /**
     * Изменение статуса заказа.
     */
    @Override
    public OrderResponse updateOrder(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            order = orderRepository.save(order);
            return Converts.orderToDto(order);
        }
        log.warn("OrderServiceImpl.updateOrder(): Order not found: {}", orderId);
        return null;
    }


    /**
     * Возврат заказа из БД по его Id
     */
    @Override
    public OrderResponse getOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElse(null);
        if (order != null) {
            return Converts.orderToDto(order);
        }
        return null;
    }


    @Override
    public void deleteOrder(String orderId) {
        orderRepository.deleteById(UUID.fromString(orderId));
    }

}
