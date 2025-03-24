package mr.demonid.service.payment.dto.events;

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
public class OrderPaidEvent {
    private UUID orderId;
    private String message;
}

