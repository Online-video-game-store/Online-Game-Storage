package mr.demonid.service.payment.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.domain.Card;
import mr.demonid.service.payment.domain.UserPayment;
import mr.demonid.service.payment.dto.CardRequest;
import mr.demonid.service.payment.dto.NewCardRequest;
import mr.demonid.service.payment.repository.CardRepository;
import mr.demonid.service.payment.repository.UserPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class UserPaymentService {

    private UserPaymentRepository userRepository;
    private CardRepository cardRepository;


    public List<CardRequest> getCards(UUID userId) {
        List<CardRequest> cards = new ArrayList<>();

        UserPayment userPayment = userRepository.findById(userId).orElse(null);
        if (userPayment != null) {
            Set<Card> c = userPayment.getCards();
            cards = c.stream().map(e -> new CardRequest(e.getId(), e.getSafeCardNumber(), e.getExpiryDate())).toList();
        }
        return cards;
    }

    // TODO: переделай времянку!!!
    public boolean addCard(NewCardRequest cardRequest) {
        UserPayment user;

        if (cardRepository.existsByCardNumber(cardRequest.getCardNumber())) {
            log.error("Card number already exists");
            return false;
        }
        Optional<UserPayment> userPayment = userRepository.findById(cardRequest.getUserId());
        user = userPayment.orElseGet(() -> new UserPayment(cardRequest.getUserId(), new HashSet<>()));
        user.addCard(new Card(null, cardRequest.getCardNumber(), cardRequest.getExpiryDate(), cardRequest.getCvv(), user));
        userRepository.save(user);
        return true;
    }

}
