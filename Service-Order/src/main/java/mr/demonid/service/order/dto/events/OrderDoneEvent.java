package mr.demonid.service.order.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


/**
 * Сообщение об успешном проведении заказа.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDoneEvent {
    private UUID orderId;
    private String message;
    private List<ProductTransferred> products;
}
