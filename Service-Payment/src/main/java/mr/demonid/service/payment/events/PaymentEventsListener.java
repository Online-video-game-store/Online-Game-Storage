package mr.demonid.service.payment.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.dto.EmptyRequest;
import mr.demonid.service.payment.dto.events.OrderPaidEvent;
import mr.demonid.service.payment.dto.events.OrderPaymentEvent;
import mr.demonid.service.payment.services.UserEntityService;
import mr.demonid.service.payment.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;


/**
 * Слушатель для канала orderEvents-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Configuration
@AllArgsConstructor
@Log4j2
public class PaymentEventsListener {

    private JwtService jwtService;
    private TokenTool tokenTool;
    private MessageMapper messageMapper;
    private PaymentPublisher paymentPublisher;
    private UserEntityService userService;


    @Bean
    public Consumer<Message<Object>> channelOrderEvents() {
        return message -> {
            try {
                String jwtToken = tokenTool.getToken(message);
                if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                    String eventType = (String) message.getHeaders().get("type");
                    log.info("-- eventType: {}", eventType);

                    if ("product.reserved".equals(eventType)) {
                        OrderPaymentEvent event = messageMapper.map(message, OrderPaymentEvent.class);
                        if (event != null) {
                            handlePaymentEvent(event);
                        }
                    } else {
                        log.warn("Неизвестный тип события: {}", eventType);
                    }
                } else {
                    log.error("Недействительный Jwt-токен");
                }

            } finally {
                log.info("-- Clean context security --");
                SecurityContextHolder.clearContext();
            }
        };
    }

    /*
     * Проведение оплаты заказа.
     */
    private void handlePaymentEvent(OrderPaymentEvent event) {
        if (userService.payment(event)) {
            paymentPublisher.sendPaymentPaid(new OrderPaidEvent(event.getOrderId(), "Оплата прошла успешно"));
        } else {
            paymentPublisher.sendPaymentCancel(event.getOrderId());
        }
    }


}
