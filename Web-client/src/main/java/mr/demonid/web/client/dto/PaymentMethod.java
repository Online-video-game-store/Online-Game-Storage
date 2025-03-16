package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMethod {
    private long id;
    private String name;
    private boolean supportsCards;
}
