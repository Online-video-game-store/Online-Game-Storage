package mr.demonid.service.catalog.dto;


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
public class OrderPaymentEvent {
    private UUID orderId;
    private String userId;
    private Long paymentId;
    private BigDecimal totalAmount;
}

