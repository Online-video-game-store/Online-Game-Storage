package mr.demonid.service.order.dto.events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


/**
 * Сообщение о не прошедшей оплате.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailEvent {
    private UUID orderId;
    private String message;
}
