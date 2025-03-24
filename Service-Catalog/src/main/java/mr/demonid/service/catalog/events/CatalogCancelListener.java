package mr.demonid.service.catalog.events;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.OrderCreatedEvent;
import mr.demonid.service.catalog.utils.TokenTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

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

    private JwtValidatorService jwtValidatorService;
    private TokenTool tokenTool;


    @Bean
    public Consumer<Message<String>> channelOrderCancel() {
        return message -> {
            String jwtToken = tokenTool.getToken(message);
            if (jwtToken != null && jwtValidatorService.validateJwt(jwtToken)) {
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
        };
    }

    /*
     * Оплата не прошла, отменяем резерв.
     */
    private void handleProcessCancel(String message) {
        // отменяем заказ
        log.info("-- cancel product reserve with message: {}", message);
    }

}
