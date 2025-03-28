package mr.demonid.service.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.service.payment.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLogResponse {
    private UUID orderId;
    private UUID userId;
    private Long paymentMethodId;
    private String cardNumber;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDate createdAt;
}
