package mr.demonid.service.payment.services;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.dto.events.OrderPaymentEvent;
import org.springframework.stereotype.Service;


/**
 * Сервис непосредственно по оплате заказов.
 */
@Service
@AllArgsConstructor
@Log4j2
public class PaymentService {

    /**
     * Оплата заказа.
     * В случае неудачи бросает исключение.
     *
     * @param order
     */
    public boolean payment(OrderPaymentEvent order) {
        log.info("-- Payment started: {}", order);
        // TODO: добавь оплату
        return true;
    }



}
