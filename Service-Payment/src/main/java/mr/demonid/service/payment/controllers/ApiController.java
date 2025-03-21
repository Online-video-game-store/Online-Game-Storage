package mr.demonid.service.payment.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.dto.CardRequest;
import mr.demonid.service.payment.dto.NewCardRequest;
import mr.demonid.service.payment.dto.PaymentMethod;
import mr.demonid.service.payment.services.UserEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/pk8000/api/payment")
@AllArgsConstructor
@Log4j2
public class ApiController {

    private UserEntityService userEntityService;


    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @GetMapping("/get-cards")
    public ResponseEntity<List<CardRequest>> getCards(@RequestParam UUID userId) {
        return ResponseEntity.ok(userEntityService.getCards(userId));
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @PostMapping("/add-card")
    public ResponseEntity<?> addCard(@RequestBody NewCardRequest card) {
        userEntityService.addCard(card);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @GetMapping("/get-payments")
    public ResponseEntity<List<PaymentMethod>> getPayments() {
        // TODO: убрать заглушку
        List<PaymentMethod> paymentMethods = List.of(
                new PaymentMethod(1L, "Visa/MasterCard", true),
                new PaymentMethod(2L, "PayPal", false),
                new PaymentMethod(3L, "SberPay", false)
        );
        return ResponseEntity.ok(paymentMethods);
    }

}
