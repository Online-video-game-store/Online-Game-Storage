package mr.demonid.service.payment.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.dto.CardResponse;
import mr.demonid.service.payment.dto.NewCardRequest;
import mr.demonid.service.payment.dto.PaymentMethodResponse;
import mr.demonid.service.payment.services.CardService;
import mr.demonid.service.payment.services.PaymentMethodService;
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

    private CardService cardService;
    private PaymentMethodService paymentMethodService;


    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @GetMapping("/get-cards")
    public ResponseEntity<List<CardResponse>> getCards(@RequestParam UUID userId) {
        return ResponseEntity.ok(cardService.getCards(userId));
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @PostMapping("/add-card")
    public ResponseEntity<?> addCard(@RequestBody NewCardRequest card) {
        cardService.addCard(card);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @GetMapping("/get-payments")
    public ResponseEntity<List<PaymentMethodResponse>> getPayments() {
        List<PaymentMethodResponse> paymentMethods = paymentMethodService.getAllMethods();
        return ResponseEntity.ok(paymentMethods);
    }

}
