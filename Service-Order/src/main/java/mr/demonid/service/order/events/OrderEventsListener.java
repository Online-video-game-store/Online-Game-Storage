package mr.demonid.service.order.events;

import com.rabbitmq.client.LongString;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.utils.TokenTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

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
public class OrderEventsListener {

    private JwtValidatorService jwtValidatorService;
    private TokenTool tokenTool;


    // TODO: заменить String на класс!!!

    @Bean
    public Consumer<Message<String>> channelOrderEvents() {
        return message -> {
            String jwtToken = tokenTool.getToken(message);
            if (jwtToken != null && jwtValidatorService.validateJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("type");

                if (Objects.requireNonNull(eventType).equals("product.transferred")) {
                    finishOrder(message.getPayload());
                } else {
                    log.warn("Неизвестный тип события: {}", eventType);
                }
            } else {
                log.error("Недействительный Jwt-токен");
            }
        };
    }

    /*
     * Успешное завершение заказа.
     */
    private void finishOrder(String message) {
        log.info("-- finish order with message: {}", message);
    }

}
