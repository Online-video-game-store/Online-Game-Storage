package mr.demonid.service.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


/**
 * Лог операций.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {
    @Id
    private UUID orderId;
    private UUID userId;
    private Long paymentMethodId;
    private Long cardId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDate createdAt;
}

