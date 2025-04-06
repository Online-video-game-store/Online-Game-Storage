package mr.demonid.service.catalog.services;

import mr.demonid.service.catalog.domain.ReservedProductEntity;
import mr.demonid.service.catalog.dto.events.*;
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

import java.util.List;


/**
 * Сервис по обработке сообщений
 */
@Service
@AllArgsConstructor
@Log4j2
public class EventService {

    private JwtService jwtService;
    private MessageMapper messageMapper;
    private TokenTool tokenTool;
    private ReservedService reservedService;
    private CatalogPublisher catalogPublisher;
    private Converts converts;


    public void doProcess(Message<Object> message) {
        try {
            String jwtToken = tokenTool.getToken(message);
            if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("routingKey");

                if ("order.created".equals(eventType)) {
                    CatalogReserveRequestEvent createdEvent = messageMapper.map(message, CatalogReserveRequestEvent.class);
                    if (createdEvent != null) {
                        handleOrderCreated(createdEvent);
                    }
                } else if ("payment.paid".equals(eventType)) {
                    PaymentPaidEvent event = messageMapper.map(message, PaymentPaidEvent.class);
                    if (event != null) {
                        handleOrderPaid(event);
                    }
                } else if ("payment.cancel".equals(eventType) || "order.stop".equals(eventType)) {
                    PaymentFailEvent event = messageMapper.map(message, PaymentFailEvent.class);
                    handlePaymentFail(event);

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
    private void handleOrderCreated(CatalogReserveRequestEvent event) {
        log.info("-- резервируем товар: {}", event);
        try {
            reservedService.reserve(event.getOrderId(), event.getItems());
            // товар успешно зарезервирован, переходим на следующий шаг.
            catalogPublisher.sendProductReserved(converts.createdToPayment(event));
        } catch (CatalogException e) {
            // сообщаем о неудаче
            catalogPublisher.sendProductCancel(new CatalogFailEvent(event.getOrderId(), e.getMessage()));
        }
    }

    /*
     * Перевод товара из резерва в службу комплектования и доставки.
     */
    private void handleOrderPaid(PaymentPaidEvent event) {
        log.info("-- отдаем товар в службу комплектации и доставки: {}", event);
        List<ProductTransferred> products = reservedService.approvedReservation(event.getOrderId());
        catalogPublisher.sendProductTransferred(
                new CatalogTransferredEvent(
                        event.getOrderId(),
                        (event.getMessage() + "&" + "Заказ передан в службу комплектации и доставки."),
                        products));
    }

    /*
     * Оплата не прошла, отменяем резерв.
     */
    private void handlePaymentFail(PaymentFailEvent event) {
        log.warn("-- отмена резерва товара: {}", event);
        reservedService.cancelReserved(event.getOrderId());
    }

}
