package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Успешно добавленные в корзину данные.
 */
@Data
@AllArgsConstructor
public class CartAddItemSuccess {
    boolean success;
    String productName;
    int quantity;
    BigDecimal totalPrice;
    int cartTotalItems;
}

