package mr.demonid.service.payment.events;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

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
public class PaymentCancelListener {

    private JwtValidatorService jwtValidatorService;
    private TokenTool tokenTool;


    @Bean
    public Consumer<Message<String>> channelOrderCancel() {
        return message -> {
            String jwtToken = tokenTool.getToken(message);
            if (jwtToken != null && jwtValidatorService.validateJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("type");

                if (Objects.requireNonNull(eventType).equals("order.stop")) {
                    handlePaymentCancel(message.getPayload());
                } else {
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
    private void handlePaymentCancel(String message) {
        // отменяем отплату
        log.info("-- cancel payment with message: {}", message);
    }

}
