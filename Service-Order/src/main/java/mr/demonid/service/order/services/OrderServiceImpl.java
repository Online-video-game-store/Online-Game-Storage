package mr.demonid.service.order.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.domain.Order;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.dto.OrderResponse;
import mr.demonid.service.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
@Transactional
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;


    /**
     * Запись нового заказа.
     */
    @Override
    public OrderResponse save(OrderCreateRequest order) {
        try {
            Order newOrder = Order.builder()
                    .userId(order.getUserId())
                    .paymentMethod(order.getPaymentId())
                    .totalAmount(order.getTotalAmount())
                    .status(OrderStatus.Pending)
                    .build();
            Order res = orderRepository.save(newOrder);
            return OrderUtil.orderToDto(res);
        } catch (Exception e) {
            log.error("OrderServiceImpl.save(): {}", e.getMessage());
        }
        return null;
    }

    /**
     * Возврат заказа из БД по его Id
     */
    @Override
    public OrderResponse getOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElse(null);
        if (order != null) {
            return OrderUtil.orderToDto(order);
        }
        return null;
    }


    @Override
    public void deleteOrder(String orderId) {
        orderRepository.deleteById(UUID.fromString(orderId));
    }

    /**
     * Изменение статуса заказа.
     */
    @Override
    public OrderResponse updateOrder(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElse(null);
        if (order != null) {
            order.setStatus(status);
            order = orderRepository.save(order);
            return OrderUtil.orderToDto(order);
        }
        return null;
    }

}
