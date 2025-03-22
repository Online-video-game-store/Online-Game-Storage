package mr.demonid.service.order.events;

import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Objects;


/**
 * Слушатель для канала orderEvents-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Configuration
@Log4j2
public class OrderEventsListener {

    @Autowired
    JwtValidatorService jwtValidatorService;


    @Bean
    public java.util.function.Consumer<Message<Object>> orderEvents() {
        return message -> {
            MessageHeaders headers = message.getHeaders();
            String jwtToken = (String) headers.get("Authorization");
            if (jwtToken != null && jwtValidatorService.validateJwt(jwtToken)) {
                String eventType = (String) headers.get("type");
                log.info("-- OrderEventsListener. type = '{}'", eventType);

                if (Objects.requireNonNull(eventType).equals("product.good")) {
                    handleOrderClose((String) message.getPayload());
                } else {
                    System.out.println("Неизвестный тип события: " + eventType);
                }
            } else {
                log.error("-- Недействительный Jwt-токен");
            }
        };
    }

    private void handleOrderClose(String orderId) {
        log.info("-- Обрабатываем событие product.good: {}", orderId);
        // закрываем заказ
    }

}
