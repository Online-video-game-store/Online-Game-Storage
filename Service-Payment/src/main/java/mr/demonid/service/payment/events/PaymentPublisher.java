package mr.demonid.service.payment.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.dto.events.PaymentPaidEvent;
import mr.demonid.service.payment.dto.events.PaymentFailEvent;
import mr.demonid.service.payment.utils.TokenTool;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


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
    public void sendPaymentPaid(PaymentPaidEvent event) {
        send("ch-pk8000-order-out", "payment.paid", event);
    }

    /**
     * Отправка сообщения о невозможности списания средств со счета пользователя.
     */
    public void sendPaymentCancel(PaymentFailEvent event) {
        send("ch-pk8000-cancel-out", "payment.cancel", event);
    }


    /*
     * Непосредственно отправка сообщения на заданный канал и с заданным ключом маршрутизации.
     */
    private void send(String channel, String routingKey, Object obj) {
        String jwtToken = tokenTool.getCurrentToken();
        if (jwtToken != null) {
            streamBridge.send(channel,
                    MessageBuilder.withPayload(obj)
                            .setHeader("routingKey", routingKey)
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие {}: {}", routingKey, obj.toString());
        }
    }
}
