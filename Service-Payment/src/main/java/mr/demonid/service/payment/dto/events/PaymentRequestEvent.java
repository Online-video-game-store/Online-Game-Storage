package mr.demonid.service.payment.dto.events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


/**
 * Сообщение на проведение платежа за заказ.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestEvent {
    private UUID orderId;
    private UUID userId;
    private Long paymentId;
    private Long cardId;
    private BigDecimal totalAmount;
}
