package mr.demonid.service.payment.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.dto.EmptyRequest;
import mr.demonid.service.payment.dto.events.OrderPaidEvent;
import mr.demonid.service.payment.utils.TokenTool;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * Отправка сообщений через Spring Cloud Bus.
 * Для безопасности в заголовок внедряем Jwt-токен текущего пользователя.
 */
@Service
@AllArgsConstructor
@Log4j2
public class PaymentPublisher {

    private StreamBridge streamBridge;
    private TokenTool tokenTool;


    /**
     * Отправка сообщения об успешном списании средств со счета пользователя.
     */
    public void sendPaymentPaid(OrderPaidEvent event) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            streamBridge.send("orderEvents-out-0",
                    MessageBuilder.withPayload(event)
                            .setHeader("type", "payment.paid")  // Задаем routing-key
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие payment.paid: {}", event);
        }
    }

    /**
     * Отправка сообщения о невозможности списания средств со счета пользователя.
     */
    public void sendPaymentCancel(UUID orderId) {
        String jwtToken = tokenTool.getToken();
        if (jwtToken != null) {
            streamBridge.send("orderCancel-out-0",
                    MessageBuilder.withPayload(orderId)
                            .setHeader("type", "payment.cancel")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.warn("Отправлено событие payment.cancel: {}", orderId);
        }
    }


}
