package mr.demonid.service.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Ответ на запрос банковских карт у юзера
 */
@Data
@AllArgsConstructor
public class CardResponse {
    private Long cardId;
    private String cardNumber;
    private String expiry;
}
