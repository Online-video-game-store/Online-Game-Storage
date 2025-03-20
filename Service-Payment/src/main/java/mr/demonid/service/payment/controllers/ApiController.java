package mr.demonid.service.payment.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.dto.CardRequest;
import mr.demonid.service.payment.dto.NewCardRequest;
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
        log.info("get-cards");
        return ResponseEntity.ok(userEntityService.getCards(userId));
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @PostMapping("/add-card")
    public ResponseEntity<?> addCard(@RequestBody NewCardRequest card) {
        log.info("-- controller: Add card");
        return ResponseEntity.ok(userEntityService.addCard(card));
    }

}
