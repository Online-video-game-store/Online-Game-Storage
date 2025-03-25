package mr.demonid.service.order.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.services.OrderService;
import mr.demonid.service.order.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;


/**
 * Слушатель для канала orderEvents-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Component
@AllArgsConstructor
@Log4j2
public class OrderEventsListener {

    private JwtService jwtService;
    private TokenTool tokenTool;
    private OrderService orderService;
    private OrderPublisher orderPublisher;


    @Bean
    public Consumer<Message<UUID>> channelOrderEvents() {
        return message -> {
            try {
                String jwtToken = tokenTool.getToken(message);
                if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                    String eventType = (String) message.getHeaders().get("routingKey");

                    if (Objects.requireNonNull(eventType).equals("product.transferred")) {
                        finishOrder(message.getPayload());
                    } else {
                        log.warn("Неизвестный тип события: {}", eventType);
                    }
                } else {
                    log.error("Недействительный Jwt-токен");
                }

            } finally {
                log.info("-- Clean security context --");
                SecurityContextHolder.clearContext();
            }
        };
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


}
