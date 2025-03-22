package mr.demonid.service.order.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.dto.OrderResponse;
import mr.demonid.service.order.events.OrderPublisher;
import mr.demonid.service.order.services.OrderService;
import mr.demonid.service.order.utils.Converts;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pk8000/api/order")
@AllArgsConstructor
@Log4j2
public class ApiController {

    private OrderService orderService;
    private OrderPublisher orderPublisher;


    /**
     * Оформление и проведение заказа.
     */
    @PostMapping("/create-order")
    public ResponseEntity<Boolean> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        log.info("-- begin crete order");
        OrderResponse order = orderService.save(orderCreateRequest);
        log.info("Order created: {}", order);
        if (order != null) {
            orderPublisher.sendCreateOrderEvent(Converts.makeOrderCreatedEvent(order.getOrderId(), orderCreateRequest));
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

}
