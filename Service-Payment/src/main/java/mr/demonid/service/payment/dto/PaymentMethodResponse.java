package mr.demonid.service.payment.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Запрос на получение списка доступных способов оплаты.
 */
@Data
@AllArgsConstructor
public class PaymentMethodResponse {
    private long id;
    private String name;
    private boolean supportsCards;
}

