package mr.demonid.service.catalog.services;

import mr.demonid.service.catalog.dto.events.OrderCancelEvent;
import mr.demonid.service.catalog.dto.events.OrderCreatedEvent;
import mr.demonid.service.catalog.dto.events.OrderPaidEvent;
import mr.demonid.service.catalog.events.CatalogPublisher;
import mr.demonid.service.catalog.exceptions.CatalogException;
import mr.demonid.service.catalog.services.tools.JwtService;
import mr.demonid.service.catalog.services.tools.MessageMapper;
import mr.demonid.service.catalog.utils.Converts;
import mr.demonid.service.catalog.utils.TokenTool;
import org.springframework.messaging.Message;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * Сервис по обработке сообщений
 */
@Service
@AllArgsConstructor
@Log4j2
public class ProcessService {

    private JwtService jwtService;
    private MessageMapper messageMapper;
    private TokenTool tokenTool;
    private ReservedService reservedService;
    private CatalogPublisher catalogPublisher;


    public void doProcess(Message<Object> message) {
        try {
            String jwtToken = tokenTool.getToken(message);
            if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("routingKey");

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
                } else if ("payment.cancel".equals(eventType) || "order.stop".equals(eventType)) {
                    OrderCancelEvent order = messageMapper.map(message, OrderCancelEvent.class);
                    handleProcessCancel(order);

                } else {
                    log.warn("Неизвестный тип события: {}", eventType);
                }

            } else {
                log.error("Недействительный Jwt-токен");
            }

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    /*
     * Резервирование товара.
     */
    private void handleOrderCreated(OrderCreatedEvent event) {
        log.info("-- резервируем товар: {}", event);
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
        log.info("-- отдаем товар в службу комплектации и доставки: {}", orderId);
        reservedService.approvedReservation(orderId);
        catalogPublisher.sendProductTransferred(orderId);
    }

    /*
     * Оплата не прошла, отменяем резерв.
     */
    private void handleProcessCancel(OrderCancelEvent event) {
        log.warn("-- отмена резерва товара: {}", event);
        reservedService.cancelReserved(event.getOrderId());
    }

}
