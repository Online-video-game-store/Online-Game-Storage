package mr.demonid.service.catalog.utils;


import mr.demonid.service.catalog.dto.events.OrderCreatedEvent;
import mr.demonid.service.catalog.dto.events.OrderPaymentEvent;

public class Converts {

    public static OrderPaymentEvent createdToPayment(OrderCreatedEvent event) {
        return new OrderPaymentEvent(
                event.getOrderId(),
                event.getUserId(),
                event.getPaymentId(),
                event.getCardId(),
                event.getTotalAmount()
        );
    }

}
