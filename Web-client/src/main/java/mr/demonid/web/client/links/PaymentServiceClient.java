package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.PaymentMethod;
import mr.demonid.web.client.dto.payment.CardRequest;
import mr.demonid.web.client.dto.payment.NewCardRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "PAYMENT-SERVICE", configuration = FeignClientConfig.class)
public interface PaymentServiceClient {

    @GetMapping("/pk8000/api/payment/get-payments")
    ResponseEntity<List<PaymentMethod>> getPayments();

    @GetMapping("/pk8000/api/payment/get-cards")
    ResponseEntity<List<CardRequest>> getCards(@RequestParam UUID userId);

    @PostMapping("/pk8000/api/payment/add-card")
    ResponseEntity<?> addCard(@RequestBody NewCardRequest card);
}
