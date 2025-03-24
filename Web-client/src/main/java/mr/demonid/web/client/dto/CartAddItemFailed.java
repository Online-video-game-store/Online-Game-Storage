package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ошибка добавления товара в корзину.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartAddItemFailed {
    private boolean success;
    private String message;
}
