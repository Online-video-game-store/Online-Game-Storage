package mr.demonid.service.catalog.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.events.OrderCancelEvent;
import mr.demonid.service.catalog.dto.events.OrderPaymentEvent;
import mr.demonid.service.catalog.dto.events.OrderTransferredEvent;
import mr.demonid.service.catalog.utils.TokenTool;
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
public class CatalogPublisher {

    private StreamBridge streamBridge;
    private TokenTool tokenTool;


    /**
     * Отправка сообщения об успешном резервировании товара.
     */
    public void sendProductReserved(OrderPaymentEvent event) {
        String jwtToken = tokenTool.getCurrentToken();
        if (jwtToken != null) {
            streamBridge.send("ch-pk8000-order-out",
                    MessageBuilder.withPayload(event)
                            .setHeader("routingKey", "product.reserved")  // Задаем routing-key
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие product.reserved: {}", event);
        }
    }

    /**
     * Отправка сообщения о списания резерва в службу набора и доставки товара.
     */
    public void sendProductTransferred(UUID orderId) {
        String jwtToken = tokenTool.getCurrentToken();
        if (jwtToken != null) {
            streamBridge.send("ch-pk8000-order-out",
                    MessageBuilder.withPayload(orderId)
                            .setHeader("routingKey", "product.transferred")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.info("-- Отправлено событие product.transferred по заказу: {}", orderId);
        }
    }


    /**
     * Отправка сообщения о невозможности зарезервировать товар.
     */
    public void sendProductCancel(OrderCancelEvent event) {
        String jwtToken = tokenTool.getCurrentToken();
        if (jwtToken != null) {
            streamBridge.send("ch-pk8000-cancel-out",
                    MessageBuilder.withPayload(event)
                            .setHeader("routingKey", "product.cancel")
                            .setHeader("Authorization", "Bearer " + jwtToken)
                            .build());
            log.warn("Отправлено событие product.cancel: {}", event);
        }
    }


}
