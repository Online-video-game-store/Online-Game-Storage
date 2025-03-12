package mr.demonid.web.client.controllers;

import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.CartOrderRequest;
import mr.demonid.web.client.dto.OrderRequest;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pk8000/user/api")
@Log4j2
public class ApiUserController {

    /**
     * Формирует и проводит заказ.
     * @param paymentMethod - тип оплаты (card, paypal, crypto)
     */
    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout(@RequestBody CartOrderRequest paymentMethod) {

        OrderRequest order = new OrderRequest(IdnUtil.getUserId(), paymentMethod.getPaymentMethod());
        log.info("  -- Checkout order request: {}", order);



        return ResponseEntity.ok().build();
    }
}

