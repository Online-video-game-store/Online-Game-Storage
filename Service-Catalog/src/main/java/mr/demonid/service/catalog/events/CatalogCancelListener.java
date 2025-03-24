package mr.demonid.service.catalog.events;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.events.OrderCancelEvent;
import mr.demonid.service.catalog.services.ReservedService;
import mr.demonid.service.catalog.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.function.Consumer;


/**
 * Слушатель для канала orderCancel-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Configuration
@AllArgsConstructor
@Log4j2
public class CatalogCancelListener {

    private ReservedService reservedService;
    private JwtService jwtService;
    private TokenTool tokenTool;


    @Bean
    public Consumer<Message<OrderCancelEvent>> channelOrderCancel() {
        return message -> {
            try {
                String jwtToken = tokenTool.getToken(message);
                if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                    String eventType = (String) message.getHeaders().get("type");

                    switch (Objects.requireNonNull(eventType)) {
                        case "payment.cancel":
                        case "order.stop":
                            handleProcessCancel(message.getPayload());
                            break;
                        default:
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
     * Оплата не прошла, отменяем резерв.
     */
    private void handleProcessCancel(OrderCancelEvent event) {
        log.warn("Cancel product reservation: {}", event);
        reservedService.cancelReserved(event.getOrderId());
    }

}
