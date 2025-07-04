package mr.demonid.service.payment.services;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.domain.PaymentLog;
import mr.demonid.service.payment.domain.PaymentStatus;
import mr.demonid.service.payment.dto.events.PaymentPaidEvent;
import mr.demonid.service.payment.dto.events.PaymentRequestEvent;
import mr.demonid.service.payment.dto.events.PaymentFailEvent;
import mr.demonid.service.payment.services.tools.JwtService;
import mr.demonid.service.payment.services.tools.MessageMapper;
import mr.demonid.service.payment.events.PaymentPublisher;
import mr.demonid.service.payment.utils.Converts;
import mr.demonid.service.payment.utils.TokenTool;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

import static java.lang.Math.random;


/**
 * Сервис непосредственно по оплате заказов.
 */
@Service
@AllArgsConstructor
@Log4j2
public class PaymentService {

    private PaymentPublisher paymentPublisher;
    private JwtService jwtService;
    private TokenTool tokenTool;
    private MessageMapper messageMapper;

    private Converts converts;
    private PaymentLogService paymentLogService;


    /**
     * Обработка входящего события по каналам "ch-pk8000-order-in" и "ch-pk8000-cancel-in"
     * Объединил в один метод, поскольку на каналах лишь по одному событию.
     */
    public void doOrderEvent(Message<Object> message) {
        try {
            String jwtToken = tokenTool.getCurrentToken(message);
            if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("routingKey");

                if ("product.reserved".equals(eventType)) {
                    PaymentRequestEvent event = messageMapper.map(message, PaymentRequestEvent.class);
                    if (event != null) {
                        handlePaymentEvent(event);
                    }
                } else if ("order.stop".equals(eventType)) {
                    UUID orderId = messageMapper.map(message, UUID.class);
                    handlePaymentCancel(orderId);
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
    }

    /*
     * Проведение оплаты заказа.
     */
    private void handlePaymentEvent(PaymentRequestEvent event) {
        if (payment(event)) {
            // Эмулируем задержку
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            paymentPublisher.sendPaymentPaid(new PaymentPaidEvent(event.getOrderId(), "Оплата прошла успешно."));
        } else {
            paymentPublisher.sendPaymentCancel(new PaymentFailEvent(event.getOrderId(), "Недостаточно средств."));
        }
    }

    /*
     * Оплата не прошла, отменяем резерв.
     */
    private void handlePaymentCancel(UUID orderId) {
        log.info("-- cancel payment with order: {}", orderId);
        // отменяем отплату
    }


    /**
     * Оплата заказа.
     */
    private boolean payment(PaymentRequestEvent order) {
        log.info("-- Payment started: {}", order);
        PaymentLog log = converts.orderToPaymentLog(order);
        log.setStatus(PaymentStatus.REQUESTED);
        log = paymentLogService.save(log);
        // проводим оплату

        // TODO: добавь оплату
        if (random() > 0.4) {
            // если всё успешно, то меняем статус
            log.setStatus(PaymentStatus.APPROVED);
            paymentLogService.save(log);
            return true;
        }

        log.setStatus(PaymentStatus.REJECTED);
        paymentLogService.save(log);
        return false;
    }

}
