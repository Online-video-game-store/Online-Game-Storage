package mr.demonid.service.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Запрос на резервирование товара.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReservationRequest {
    private UUID orderId;
    private long userId;
    private long productId;
    private int quantity;
    private BigDecimal price;
}
