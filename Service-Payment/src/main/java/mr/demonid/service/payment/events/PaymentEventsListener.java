package mr.demonid.service.payment.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.dto.events.OrderPaidEvent;
import mr.demonid.service.payment.dto.events.OrderPaymentEvent;
import mr.demonid.service.payment.services.CardService;
import mr.demonid.service.payment.services.PaymentService;
import mr.demonid.service.payment.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


/**
 * Слушатель для канала orderEvents-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Component
@AllArgsConstructor
@Log4j2
public class PaymentEventsListener {

    private PaymentService paymentService;


    @Bean
    public Consumer<Message<Object>> channelOrderEvents() {
        return message -> paymentService.doOrderEvent(message);
    }

}
