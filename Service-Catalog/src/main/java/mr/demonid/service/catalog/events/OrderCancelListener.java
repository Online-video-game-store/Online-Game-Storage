package mr.demonid.service.catalog.events;


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

                if (Objects.requireNonNull(eventType).equals("payment.cancel")) {
                    handleProcessCancel();
                } else {
                    log.error("Неизвестный тип события: {}", eventType);
                }
            } else {
                log.error("-- Недействительный Jwt-токен");
            }
        };
    }

    /*
        Оплата не прошла, отменяем резерв и прокидываем сообщение дальше.
     */
    private void handleProcessCancel() {
        // отменяем заказ

    }
}
