package mr.demonid.service.catalog.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Сообщение об ошибке резервирования товара.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogFailEvent {
    UUID orderId;
    String message;
}
