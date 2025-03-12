package mr.demonid.web.client.dto;


import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Класс для начала оплаты товаров из корзины.
 */
@Data
@AllArgsConstructor
public class CartOrderRequest {
    private String paymentMethod;
}
