package mr.demonid.service.order.services;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.dto.events.*;
import mr.demonid.service.order.events.OrderPublisher;
import mr.demonid.service.order.services.tools.JwtService;
import mr.demonid.service.order.services.tools.MessageMapper;
import mr.demonid.service.order.utils.TokenTool;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
                    CatalogTransferredEvent event = messageMapper.map(message, CatalogTransferredEvent.class);
                    finishOrder(event);
                } else if ("product.cancel".equals(eventType)) {
                    CatalogFailEvent event = messageMapper.map(message, CatalogFailEvent.class);
                    orderCancel(new OrderFailEvent(event.getOrderId(), event.getMessage()));
                } else if ("payment.cancel".equals(eventType)) {
                    PaymentFailEvent event = messageMapper.map(message, PaymentFailEvent.class);
                    orderCancel(new OrderFailEvent(event.getOrderId(), event.getMessage()));
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
    private void finishOrder(CatalogTransferredEvent event)
    {
        log.info("-- успешное оформление заказа: {}", event);
        orderService.updateOrder(event.getOrderId(), OrderStatus.Approved);
        // Извещаем веб-сервис об успешном оформлении заказа
        orderPublisher.sendFinishOrderEvent(new OrderDoneEvent(event.getOrderId(), event.getMessage(), event.getProducts()));
    }

    /*
    Заказ завершился ошибкой.
    */
    private void orderCancel(OrderFailEvent event) {
        log.error("-- заказ завершился ошибкой: {}", event);
        orderService.updateOrder(event.getOrderId(), OrderStatus.Cancelled);
        orderPublisher.sendFailOrderEvent(event);
    }

}
