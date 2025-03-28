package mr.demonid.service.catalog.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.events.OrderCancelEvent;
import mr.demonid.service.catalog.dto.events.OrderCreatedEvent;
import mr.demonid.service.catalog.dto.events.OrderPaidEvent;
import mr.demonid.service.catalog.exceptions.CatalogException;
import mr.demonid.service.catalog.services.ReservedService;
import mr.demonid.service.catalog.utils.Converts;
import mr.demonid.service.catalog.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Consumer;


/**
 * Слушатель для канала orderEvents-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Component
@AllArgsConstructor
@Log4j2
public class CatalogEventsListener {

    private MessageMapper messageMapper;
    private ReservedService reservedService;
    private CatalogPublisher catalogPublisher;


    @Bean
    public Consumer<Message<Object>> channelOrderEvents() {
        return message -> {
            if (isAuthenticated()) {
                String eventType = (String) message.getHeaders().get("routingKey");
                log.info("-- receive eventType: {}", eventType);

                if ("order.created".equals(eventType)) {
                    OrderCreatedEvent createdEvent = messageMapper.map(message, OrderCreatedEvent.class);
                    if (createdEvent != null) {
                        handleOrderCreated(createdEvent);
                    }
                } else if ("payment.paid".equals(eventType)) {
                    OrderPaidEvent paidEvent = messageMapper.map(message, OrderPaidEvent.class);
                    if (paidEvent != null) {
                        handleOrderPaid(paidEvent.getOrderId());
                    }
                } else {
                    log.warn("Неизвестный тип события: {}", eventType);
                }
            } else {
                log.error("Anonimous!");
            }
        };
    }
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /*
     * Резервирование товара.
     */
    private void handleOrderCreated(OrderCreatedEvent event) {
        log.info("-- Обрабатываем событие order.created: {}", event);
        try {
            reservedService.reserve(event.getOrderId(), event.getItems());
            // товар успешно зарезервирован, переходим на следующий шаг.
            catalogPublisher.sendProductReserved(Converts.createdToPayment(event));
        } catch (CatalogException e) {
            // сообщаем о неудаче
            catalogPublisher.sendProductCancel(new OrderCancelEvent(event.getOrderId(), e.getMessage()));
        }
    }

    /*
     * Перевод товара из резерва в службу комплектования и доставки.
     */
    private void handleOrderPaid(UUID orderId) {
        log.info("-- Обрабатываем событие payment.paid: {}", orderId);
        reservedService.approvedReservation(orderId);
        catalogPublisher.sendProductTransferred(orderId);
    }

}
