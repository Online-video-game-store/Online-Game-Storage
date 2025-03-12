package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Класс для формирования заказа на стороне клиента и отправке его в соответствующий микросервис.
 * Только самое необходимое, остальное микросервис получит самостоятельно.
 */
@Data
@AllArgsConstructor
public class OrderRequest {
    private String userId;
    private String payMethod;
}
