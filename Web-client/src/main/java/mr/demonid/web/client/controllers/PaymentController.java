package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.web.client.dto.*;
import mr.demonid.web.client.dto.payment.CardRequest;
import mr.demonid.web.client.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/pk8000/catalog/payment")
public class PaymentController {

    private PaymentService paymentService;


    /**
     * Отображает страницу выбора платежной системы
     */
    @GetMapping
    public String showPaymentPage(Model model) {
        log.info("== showPaymentPage");

        // доступные платежные системы
        List<PaymentMethod> paymentMethods = List.of(
            new PaymentMethod(1L, "Visa/MasterCard", true),
            new PaymentMethod(2L, "PayPal", false),
            new PaymentMethod(3L, "Google Pay", false)
        );

        // карты пользователя
        List<CardRequest> userCards = paymentService.getCards();

//        List<UserCard> userCards = List.of(
//            new UserCard(101L, "**** **** **** 1234", "12/25"),
//            new UserCard(102L, "**** **** **** 5678", "06/24")
//        );
//
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("userCards", userCards);

        return "/payment";
    }


    /**
     * Добавление новой банковской карты.
     */
    @PostMapping("/add-card")
    @ResponseBody
    public ResponseEntity<?> addCard(@RequestBody AddCardRequest request) {
        log.info("== addCard");
        if (request.getCardNumber().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Некорректный номер карты"));
        }
        if (paymentService.addNewCard(request)) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Карта добавлена!"));
        }
        return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Ошибка добавления карты. Попробуйте попозже."));
    }


    /**
     * Формирование заказа.
     */
    @PostMapping("/process")
    @ResponseBody
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest request) {
        log.info("== processPayment: {}", request);
        if (request.getPaymentMethodId() == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Выберите платежную систему"));
        }

        boolean success = Math.random() > 0.3; // Симуляция успеха 70%
        if (success) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Оплата успешна!"));
        } else {
            return ResponseEntity.ok(Map.of("status", "error", "message", "Ошибка оплаты"));
        }
    }
}
