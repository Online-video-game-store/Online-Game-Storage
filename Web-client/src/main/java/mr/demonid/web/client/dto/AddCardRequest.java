package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCardRequest {
    private String cardNumber;
    private String expiryDate;
    private String cvv;

}
