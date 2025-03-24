package mr.demonid.service.catalog.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


/**
 * Сообщение об отправке купленного товара в службу комплектации и доставки.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTransferredEvent {
    private UUID orderId;
    private String message;
}
