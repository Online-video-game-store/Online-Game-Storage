package mr.demonid.service.catalog.utils;


import mr.demonid.service.catalog.dto.events.CatalogReserveRequestEvent;
import mr.demonid.service.catalog.dto.events.PaymentRequestEvent;

public class Converts {

    public static PaymentRequestEvent createdToPayment(CatalogReserveRequestEvent event) {
        return new PaymentRequestEvent(
                event.getOrderId(),
                event.getUserId(),
                event.getPaymentId(),
                event.getCardId(),
                event.getTotalAmount()
        );
    }

}
