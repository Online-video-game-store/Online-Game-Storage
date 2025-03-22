package mr.demonid.service.order.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Таблица заказов.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private Long paymentId;

    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    /**
     * Порождающий паттерн Builder.
     * И почему в Java нет #define? Сколько работы можно было сократить...
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Order order;

        public Builder() {
            order = new Order();
            order.status = OrderStatus.Bad;
        }

        public Builder orderId(UUID orderId) {
            order.orderId = orderId;
            return this;
        }

        public Builder userId(String userId) {
            order.userId = userId;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Стоимость не может быть меньше и равной нулю");
            }
            order.totalAmount = totalAmount;
            return this;
        }

        public Builder paymentMethod(Long paymentId) {
            // дальше проверится
            if (paymentId == null || paymentId == 0L) {
                throw new IllegalArgumentException("Неверный метод оплаты");
            }
            order.paymentId = paymentId;
            return this;
        }

        public Builder createAt(LocalDateTime createAt) {
            order.createAt = createAt;
            return this;
        }

        public Builder createAtNow() {
            order.createAt = LocalDateTime.now();
            return this;
        }

        public Builder status(OrderStatus status) {
            order.status = status;
            return this;
        }

        public Order build() {
            if (order.createAt == null) {
                order.createAt = LocalDateTime.now();
            }
            return order;
        }
    }

}
