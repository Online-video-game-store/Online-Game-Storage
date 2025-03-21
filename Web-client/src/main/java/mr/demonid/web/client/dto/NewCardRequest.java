package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewCardRequest {
    private String cardNumber;
    private String expiryDate;
    private String cvv;

}
