package mr.demonid.service.order.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pk8000/api/order")
@AllArgsConstructor
@Log4j2
public class ApiController {

    private OrderService orderService;


    /**
     * Оформление и проведение заказа.
     */
    @PostMapping("/create-order")
    public ResponseEntity<Boolean> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        log.info("-- begin crete order: {}", orderCreateRequest);
        orderService.createOrder(orderCreateRequest);
        return ResponseEntity.ok(true);
    }

}
