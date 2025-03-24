package mr.demonid.service.order.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.dto.OrderCreatedEvent;
import mr.demonid.service.order.utils.TokenTool;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;


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


    /**
     * Отправка сообщения о создании заказа.
     */
    public void sendCreateOrderEvent(OrderCreatedEvent order) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            streamBridge.send("orderEvents-out-0",
                    MessageBuilder.withPayload(order)
                            .setHeader("type", "order.created")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие order.created: {}", order);
        }
    }

    /**
     * Отправка сообщения об аварийной остановке заказа.
     */
    public void sendStopOrderEvent(Long orderId) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            streamBridge.send("orderCancel-out-0",
                    MessageBuilder.withPayload(orderId)
                            .setHeader("type", "order.stop")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.warn("Отправлено событие order.stop: {}", orderId);
        }
    }


}
