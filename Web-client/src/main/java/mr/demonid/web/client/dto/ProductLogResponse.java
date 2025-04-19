package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLogResponse {
    private UUID orderId;
    private String name;
    private int quantity;
    private ReservationStatus reservationStatus;
}
