package mr.demonid.web.client.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.web.client.dto.CartItemResponse;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    private String userId;
    private Long paymentId;
    private Long cardId;
    private BigDecimal totalAmount;
    private List<CartItemResponse> items;
}
