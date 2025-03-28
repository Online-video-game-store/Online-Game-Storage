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
public class PaymentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID orderId;
    private UUID userId;
    private Long paymentMethodId;
    private String cardNumber;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDate createdAt;
}

