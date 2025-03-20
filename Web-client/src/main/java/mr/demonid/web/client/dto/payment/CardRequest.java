package mr.demonid.web.client.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Ответ на запрос банковских карт у юзера
 */
@Data
@AllArgsConstructor
public class CardRequest {
    private Long cardId;
    private String cardNumber;
    private String expiry;
}
