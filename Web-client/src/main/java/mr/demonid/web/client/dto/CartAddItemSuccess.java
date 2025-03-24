package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Успешно добавленные в корзину данные.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartAddItemSuccess {
    private boolean success;
    private String productName;
    private int quantity;
    private BigDecimal totalPrice;
    private int cartTotalItems;
}

