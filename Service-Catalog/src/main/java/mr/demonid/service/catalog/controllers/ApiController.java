package mr.demonid.service.catalog.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.services.ReservedService;
import mr.demonid.store.commons.dto.ProductReservationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pk8000/api/catalog")
@AllArgsConstructor
@Log4j2
public class ApiController {

    ReservedService reservedService;

    /**
     * Резервирование товара.
     * @param request Параметры запроса (код товара, кто резервирует, сколько)
     */
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveCatalog(@RequestBody ProductReservationRequest request) {
        log.info("-- reserve request: {}", request.toString());
        reservedService.reserve(request);
        return ResponseEntity.ok("Товар зарезервирован.");
    }

    /**
     * Отмена резерва.
     */
    @PostMapping("/cancel")
    public ResponseEntity<Void> unblock(@RequestBody UUID orderId) {
        log.info("-- unblock request: {}", orderId.toString());
        reservedService.cancelReserved(orderId);
        return ResponseEntity.ok().build();
    }

    /**
     * Завершение заказа, списываем его из резерва.
     */
    @PostMapping("/approved")
    public ResponseEntity<Void> approve(@RequestBody UUID orderId) {
        log.info("-- approve request: {}", orderId.toString());
        reservedService.approvedReservation(orderId);
        return ResponseEntity.ok().build();
    }

}
