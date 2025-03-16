package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {
    private String paymentMethodId;
    private String cardId;

}
