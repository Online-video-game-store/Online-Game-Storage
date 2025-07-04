package mr.demonid.service.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    private UUID userId;
    private Long paymentId;
    private Long cardId;
    private BigDecimal totalAmount;
    private List<CartItemResponse> items;
}
