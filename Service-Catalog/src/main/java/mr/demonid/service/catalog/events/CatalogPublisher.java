package mr.demonid.service.catalog.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.OrderPaymentEvent;
import mr.demonid.service.catalog.utils.TokenTool;
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
    private TokenTool tokenTool;


    /**
     * Отправка сообщения об успешном резервировании товара.
     */
    public void sendProductReserved(OrderPaymentEvent event) {
        String jwtToken = tokenTool.getToken();
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
     * Отправка сообщения о списания резерва в службу набора и доставки товара.
     */
    public void sendProductTransferred(String message) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            streamBridge.send("orderEvents-out-0",
                    MessageBuilder.withPayload(message)
                            .setHeader("type", "product.transferred")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие product.transferred: {}", message);
        }
    }


    /**
     * Отправка сообщения о невозможности зарезервировать товар.
     */
    public void sendProductCancel(String errorMessage) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            streamBridge.send("orderCancel-out-0",
                    MessageBuilder.withPayload(errorMessage)
                            .setHeader("type", "product.cancel")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.warn("Отправлено событие product.cancel: {}", errorMessage);
        }
    }


}
