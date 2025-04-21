package mr.demonid.service.order.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.dto.OrderCreateRequest;
import mr.demonid.service.order.dto.OrderResponse;
import mr.demonid.service.order.dto.filters.OrderFilter;
import mr.demonid.service.order.services.OrderService;
import mr.demonid.store.commons.dto.PageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        orderService.createOrder(orderCreateRequest);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/get_orders")
    public ResponseEntity<PageDTO<OrderResponse>> getOrders(@RequestBody OrderFilter orderFilter, Pageable page) {
        return ResponseEntity.ok(new PageDTO<>(orderService.getAllOrders(orderFilter, page)));
    }
}
