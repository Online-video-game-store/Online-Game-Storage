package mr.demonid.service.catalog.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.events.CatalogFailEvent;
import mr.demonid.service.catalog.dto.events.PaymentRequestEvent;
import mr.demonid.service.catalog.dto.events.CatalogTransferredEvent;
import mr.demonid.service.catalog.utils.TokenTool;
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
public class CatalogPublisher {

    private StreamBridge streamBridge;
    private TokenTool tokenTool;


    /**
     * Отправка сообщения об успешном резервировании товара.
     */
    public void sendProductReserved(PaymentRequestEvent event) {
        send("ch-pk8000-order-out", "product.reserved", event);
    }

    /**
     * Отправка сообщения о списания резерва в службу набора и доставки товара.
     */
    public void sendProductTransferred(CatalogTransferredEvent event) {
        send("ch-pk8000-order-out", "product.transferred", event);
    }


    /**
     * Отправка сообщения о невозможности зарезервировать товар.
     */
    public void sendProductCancel(CatalogFailEvent event) {
        send("ch-pk8000-cancel-out", "product.cancel", event);
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
