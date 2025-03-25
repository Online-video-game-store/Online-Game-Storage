package mr.demonid.service.order.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.dto.events.OrderCreatedEvent;
import mr.demonid.service.order.utils.TokenTool;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * Отправка сообщений через Spring Cloud Bus.
 * Для безопасности в заголовок внедряем Jwt-токен текущего пользователя.
 */
@Service
@AllArgsConstructor
@Log4j2
public class OrderPublisher {

    private StreamBridge streamBridge;
    private TokenTool tokenTool;

    private RabbitTemplate rabbitTemplate;

    /**
     * Отправка сообщения о создании заказа.
     */
    public void sendCreateOrderEvent(OrderCreatedEvent order) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            Message<OrderCreatedEvent> message = MessageBuilder.withPayload(order)
                    .setHeader("routingKey", "order.created")
                    .setHeader("Authorization", "Bearer " + jwtToken)
                    .build();
            streamBridge.send("ch-pk8000-order-out", message);
            log.info("-- Отправлено событие order.created: {}", order);
        }
    }


    /**
     * Отправка сообщения об успешном завершении заказа.
     */
    public void sendFinishOrderEvent(UUID orderId) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            streamBridge.send("ch-pk8000-order-out",
                    MessageBuilder.withPayload(orderId)
                            .setHeader("routingKey", "order.done")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие order.done: {}", orderId);
        }
    }

    /**
     * Отправка сообщения о неудаче оформления заказа
     */
    public void sendFailOrderEvent(UUID orderId) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            streamBridge.send("ch-pk8000-order-out",
                    MessageBuilder.withPayload(orderId)
                            .setHeader("routingKey", "order.fail")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие order.fail: {}", orderId);
        }
    }



    /**
     * Отправка сообщения об аварийной остановке заказа.
     */
    public void sendStopOrderEvent(Long orderId) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            streamBridge.send("ch-pk8000-cancel-out",
                    MessageBuilder.withPayload(orderId)
                            .setHeader("routingKey", "order.stop")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.warn("Отправлено событие order.stop: {}", orderId);
        }
    }


}
