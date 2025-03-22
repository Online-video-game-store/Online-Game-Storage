package mr.demonid.service.order.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.dto.OrderCreatedEvent;
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


    /**
     * Отправка сообщения "order.created"
     */
    public void sendCreateOrderEvent(OrderCreatedEvent order) {
        String jwtToken = getToken();
        if (jwtToken != null) {
            streamBridge.send("orderEvents-out-0",
                    MessageBuilder.withPayload(order)
                            .setHeader("type", "order.created")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие order.created: {}", order);
        }
    }


    /*
        Получение Jwt-токена из текущего контекста SecurityContext
    */
    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            // Пользователь авторизован через Jwt
            Jwt jwt = jwtToken.getToken();
            return jwt.getTokenValue();
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            // Пользователь авторизован через OIDC
            return oidcUser.getIdToken().getTokenValue();
        }
        // Пользователь не авторизован
        log.error("-- getToken(): This user is anonymous");
        return null;
    }
}
