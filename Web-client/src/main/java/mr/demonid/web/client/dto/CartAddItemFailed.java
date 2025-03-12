package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Ошибка добавления товара в корзину.
 */
@Data
@AllArgsConstructor
public class CartAddItemFailed {
    boolean success;
    String message;
}
