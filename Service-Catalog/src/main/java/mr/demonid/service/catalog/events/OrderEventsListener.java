package mr.demonid.service.catalog.events;

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

                switch (Objects.requireNonNull(eventType)) {
                    case "order.created":
                        handleOrderCreated((OrderCreatedEvent) message.getPayload());
                        break;
                    case "payment.good":
//                    handlePaymentGood((PaymentGoodEvent) message.getPayload());
                        break;
                    default:
                        System.out.println("Неизвестный тип события: " + eventType);
                }
            } else {
                log.error("-- Недействительный Jwt-токен");
            }
        };
    }

    private void handleOrderCreated(OrderCreatedEvent event) {
        log.info("-- Обрабатываем событие order.created: {}", event);
        // резервируем товар


    }

//    private void handlePaymentGood(PaymentGoodEvent event) {
//        System.out.println("Обрабатываем событие payment.good: " + event);
//        // пришло подтверждение оплаты, передаем товары из резерва в службу сборки
//    }

}
