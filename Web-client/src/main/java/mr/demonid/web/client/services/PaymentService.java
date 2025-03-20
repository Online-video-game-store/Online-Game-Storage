package mr.demonid.web.client.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.AddCardRequest;
import mr.demonid.web.client.dto.payment.CardRequest;
import mr.demonid.web.client.dto.payment.NewCardRequest;
import mr.demonid.web.client.links.PaymentServiceClient;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class PaymentService {

    private PaymentServiceClient paymentServiceClient;


    public List<CardRequest> getCards() {
        try {
            String userId = IdnUtil.getUserId();
            if (userId != null) {
                return paymentServiceClient.getCards(UUID.fromString(userId)).getBody();
            }
        } catch (FeignException e) {
            log.error("PaymentService.getCards(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return List.of();
    }

    public boolean addNewCard(AddCardRequest cardRequest) {
        try {
            String userId = IdnUtil.getUserId();
            if (userId != null) {
                log.info("-- Adding new card user: {}", userId);
                paymentServiceClient.addCard(new NewCardRequest(UUID.fromString(userId), cardRequest.getCardNumber(), cardRequest.getExpiryDate(), cardRequest.getCvv()));
                return true;
            }
        } catch (FeignException e) {
            log.error("PaymentService.addNewCard(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return false;
    }
}
