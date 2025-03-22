package mr.demonid.service.catalog.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.OrderPaymentEvent;
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
public class CatalogPublisher {

    private StreamBridge streamBridge;


    /**
     * Отправка сообщения 'product.reserved'
     */
    public void sendProductReserved(OrderPaymentEvent event) {
        String jwtToken = getToken();
        if (jwtToken != null) {
            streamBridge.send("orderEvents-out-0",
                    MessageBuilder.withPayload(event)
                            .setHeader("type", "product.reserved")  // Задаем routing-key
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие product.reserved: {}", event);
        }
    }

    /**
     * Отправка сообщения 'product.good'
     */
    public void sendProductGood(String message) {
        String jwtToken = getToken();
        if (jwtToken != null) {
            streamBridge.send("orderEvents-out-0",
                    MessageBuilder.withPayload(message)
                            .setHeader("type", "product.good")
                            .build());
            log.info("-- Отправлено событие product.good: {}", message);
        }
    }


    /**
     * Отправка сообщения о невозможности зарезервировать товар.
     */
    public void sendError(String errorMessage) {
        String jwtToken = getToken();
        if (jwtToken != null) {
            streamBridge.send("orderCancel-out-0",
                    MessageBuilder.withPayload(errorMessage)
                            .setHeader("error.type", "processing.error")
                            .build());
            System.out.println("Отправлено сообщение об ошибке: " + errorMessage);
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
        } else {
            // Пользователь не авторизован
            log.error("-- getToken(): This user is anonymous");
        }
        return null;
    }

}
