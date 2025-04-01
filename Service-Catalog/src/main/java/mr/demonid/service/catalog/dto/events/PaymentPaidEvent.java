package mr.demonid.service.catalog.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Сообщение о том, что заказ успешно оплачен
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPaidEvent {
    private UUID orderId;
    private String message;
}

