package mr.demonid.service.order.events;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Objects;


/**
 * Слушатель для канала orderCancel-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Configuration
@Log4j2
public class OrderCancelListener {

    @Autowired
    private JwtValidatorService jwtValidatorService;


    @Bean
    public java.util.function.Consumer<Message<Object>> orderCancelListener() {
        return message -> {
            MessageHeaders headers = message.getHeaders();
            String jwtToken = (String) headers.get("Authorization");
            if (jwtToken != null && jwtValidatorService.validateJwt(jwtToken)) {
                String eventType = (String) headers.get("type");
                log.warn("-- OrderCancelListener. type = '{}'", eventType);

                switch (Objects.requireNonNull(eventType)) {
                    case "product.cancel":
                    case "payment.cancel":
                        handleOrderCancelled((String) message.getPayload());
                        break;
                    default:
                        System.out.println("Неизвестный тип события: " + eventType);
                }
            } else {
                log.error("-- Недействительный Jwt-токен");
            }
        };
    }

    /*
        Оплата не прошла, отменяем резерв и прокидываем сообщение дальше.
     */
    private void handleOrderCancelled(String orderId) {
        // отменяем заказ

    }
}
