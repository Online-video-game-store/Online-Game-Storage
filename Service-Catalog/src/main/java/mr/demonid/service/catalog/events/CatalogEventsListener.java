package mr.demonid.service.catalog.events;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.events.OrderCancelEvent;
import mr.demonid.service.catalog.dto.events.OrderCreatedEvent;
import mr.demonid.service.catalog.dto.events.OrderPaidEvent;
import mr.demonid.service.catalog.dto.events.OrderTransferredEvent;
import mr.demonid.service.catalog.exceptions.CatalogException;
import mr.demonid.service.catalog.services.ReservedService;
import mr.demonid.service.catalog.utils.Converts;
import mr.demonid.service.catalog.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;

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
public class CatalogEventsListener {

    private JwtService jwtService;
    private MessageMapper messageMapper;
    private TokenTool tokenTool;
    private ReservedService reservedService;
    private CatalogPublisher catalogPublisher;


    @Bean
    public Consumer<Message<Object>> channelOrderEvents() {
        return message -> {
            try {
                String jwtToken = tokenTool.getToken(message);
                if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                    String eventType = (String) message.getHeaders().get("type");
                    log.info("-- eventType: {}", eventType);
                    if (eventType != null) {
                        switch (eventType) {
                            case "order.created":
                                OrderCreatedEvent createdEvent = messageMapper.map(message, OrderCreatedEvent.class);
                                if (createdEvent != null) {
                                    handleOrderCreated(createdEvent);
                                }
                                break;
                            case "payment.paid":
                                OrderPaidEvent paidEvent = messageMapper.map(message, OrderPaidEvent.class);
                                if (paidEvent != null) {
                                    handleOrderPaid(paidEvent);
                                }
                                break;
                            default:
                                log.warn("Неизвестный тип события: {}", eventType);
                        }
                    }
                } else {
                    log.error("Недействительный Jwt-токен");
                }

            } finally {
                log.info("-- Clean security context --");
                SecurityContextHolder.clearContext();
            }
        };
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
    private void handleOrderPaid(OrderPaidEvent event) {
        log.info("-- Обрабатываем событие payment.paid: {}", event);
        reservedService.approvedReservation(event.getOrderId());
        catalogPublisher.sendProductTransferred(new OrderTransferredEvent(event.getOrderId(), "Заказ передан в службу комплектации и доставки"));
    }

}
