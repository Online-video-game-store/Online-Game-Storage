package mr.demonid.service.order.services;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.events.OrderPublisher;
import mr.demonid.service.order.services.tools.JwtService;
import mr.demonid.service.order.services.tools.MessageMapper;
import mr.demonid.service.order.utils.TokenTool;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class EventsService {

    private JwtService jwtService;
    private TokenTool tokenTool;
    private OrderService orderService;
    private OrderPublisher orderPublisher;
    private MessageMapper messageMapper;


    public void doProcess(Message<Object> message) {
        try {
            String jwtToken = tokenTool.getCurrentToken(message);
            if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("routingKey");

                if ("product.transferred".equals(eventType)) {
                    UUID orderId = messageMapper.map(message, UUID.class);
                    finishOrder(orderId);
                } else if ("product.cancel".equals(eventType) || "payment.cancel".equals(eventType)) {
                    UUID orderId = messageMapper.map(message, UUID.class);
                    orderCancel(orderId);

                } else {
                    log.warn("Неизвестный тип события: {}", eventType);
                }

            } else {
                log.error("Недействительный Jwt-токен");
            }

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    /*
     * Успешное завершение заказа.
     */
    private void finishOrder(UUID orderId)
    {
        log.info("-- finish order: {}", orderId);
        orderService.updateOrder(orderId, OrderStatus.Approved);
        // Извещаем веб-сервис об успешном оформлении заказа
        orderPublisher.sendFinishOrderEvent(orderId);
    }

    /*
    Заказ завершился ошибкой.
    */
    private void orderCancel(UUID orderId) {
        log.info("-- order {} cancelled", orderId);
        orderService.updateOrder(orderId, OrderStatus.Cancelled);
        orderPublisher.sendFailOrderEvent(orderId);
    }

}
