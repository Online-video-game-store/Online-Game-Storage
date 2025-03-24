package mr.demonid.service.catalog.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Сообщение об ошибке.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelEvent {
    UUID orderId;
    String message;
}
