package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.web.client.dto.*;
import mr.demonid.web.client.dto.payment.CardRequest;
import mr.demonid.web.client.services.OrderService;
import mr.demonid.web.client.services.PaymentService;
import mr.demonid.web.client.utils.IdnUtil;
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
    private OrderService orderService;


    /**
     * Отображает страницу выбора платежной системы
     */
    @GetMapping
    public String showPaymentPage(Model model) {
        List<PaymentMethod> paymentMethods = paymentService.getPaymentMethods();
        List<CardRequest> userCards = paymentService.getCards();
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("userCards", userCards);
        return "/payment";
    }


    /**
     * Добавление новой банковской карты.
     */
    @PostMapping("/add-card")
    @ResponseBody
    public ResponseEntity<?> addCard(@RequestBody NewCardRequest request) {
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
        // Формируем заказ
        if (orderService.createOrder(request)) {
            log.info("-- order proceed...");
            return ResponseEntity.ok(Map.of("status", "success", "message", "Оплата успешна!"));
        }
        log.error("-- order can't proceed");
        return ResponseEntity.ok(Map.of("status", "error", "message", "Ошибка оплаты. Попробуйте позже."));
    }

}
