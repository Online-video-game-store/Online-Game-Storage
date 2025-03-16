package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCard {
    private Long id;
    private String maskedNumber;
    private String expiry;
}
