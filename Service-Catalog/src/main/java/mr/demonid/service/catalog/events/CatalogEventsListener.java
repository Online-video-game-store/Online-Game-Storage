package mr.demonid.service.catalog.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.OrderCreatedEvent;
import mr.demonid.service.catalog.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.Objects;
import java.util.function.Consumer;


/**
 * Слушатель для канала orderEvents-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Configuration
@AllArgsConstructor
@Log4j2
public class CatalogEventsListener {

    private JwtValidatorService jwtValidatorService;
    private MessageMapper messageMapper;
    private TokenTool tokenTool;


    @Bean
    public Consumer<Message<Object>> channelOrderEvents() {
        return message -> {
            String jwtToken = tokenTool.getToken(message);
            if (jwtToken != null && jwtValidatorService.validateJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("type");

                switch (Objects.requireNonNull(eventType)) {
                    case "order.created":
                        OrderCreatedEvent event = messageMapper.map(message, OrderCreatedEvent.class);
                        if (event != null) {
                            handleOrderCreated(event);
                        }
                        break;
                    case "payment.paid":
                        break;
                    default:
                        log.warn("Неизвестный тип события: {}", eventType);
                }
            } else {
                log.error("Недействительный Jwt-токен");
            }
        };
    }

    private void handleOrderCreated(OrderCreatedEvent event) {
        log.info("-- Обрабатываем событие order.created: {}", event);
        // резервируем товар


    }

}
