package mr.demonid.web.client.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для CartItem
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemRequest {
    private Long productId;
    private int quantity;
}
