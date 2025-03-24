package mr.demonid.service.payment.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.domain.Card;
import mr.demonid.service.payment.domain.UserEntity;
import mr.demonid.service.payment.dto.CardRequest;
import mr.demonid.service.payment.dto.NewCardRequest;
import mr.demonid.service.payment.dto.events.OrderPaymentEvent;
import mr.demonid.service.payment.exceptions.AddCardException;
import mr.demonid.service.payment.repository.CardRepository;
import mr.demonid.service.payment.repository.UserEntityRepository;
import mr.demonid.service.payment.utils.CardUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class UserEntityService {

    private UserEntityRepository userEntityRepository;
    private CardRepository cardRepository;


    /**
     * Возвращает список банковских карт пользователя.
     * Номера карт частично скрыты.
     */
    public List<CardRequest> getCards(UUID userId) {
        List<CardRequest> cards = new ArrayList<>();

        UserEntity userEntity = userEntityRepository.findById(userId).orElse(null);
        if (userEntity != null) {
            Set<Card> c = userEntity.getCards();
            cards = c.stream().map(e -> new CardRequest(e.getId(), e.getSafeCardNumber(), e.getExpiryDate())).toList();
        }
        return cards;
    }


    /**
     * Привязка банковской карты к пользователю.
     * В случае ошибки кидает исключение для глобального обработчика.
     */
    public void addCard(NewCardRequest cardRequest) {
        UserEntity userEntity;

        if (!CardUtil.isCardNumberValid(cardRequest.getCardNumber())
                || !CardUtil.isExpiryDateValid(cardRequest.getExpiryDate())
                || !CardUtil.isCvvValid(cardRequest.getCvv())) {
            log.error("Bad card format");
            throw new AddCardException("Неверный формат данных.");
        }

        if (cardRepository.existsByCardNumber(cardRequest.getCardNumber())) {
            log.error("Card number already exists");
            throw new AddCardException("Карта уже существует.");
        }
        try {
            Optional<UserEntity> userPayment = userEntityRepository.findById(cardRequest.getUserId());
            userEntity = userPayment.orElseGet(() -> new UserEntity(cardRequest.getUserId(), new HashSet<>()));
            userEntity.addCard(new Card(null, cardRequest.getCardNumber(), cardRequest.getExpiryDate(), cardRequest.getCvv(), userEntity));
            userEntityRepository.save(userEntity);
        } catch (Exception e) {
            throw new AddCardException(e.getMessage());
        }
    }


    /**
     * Оплата заказа.
     * В случае неудачи бросает исключение.
     *
     * @param order
     */
    public boolean payment(OrderPaymentEvent order) {
        log.info("-- Payment started: {}", order);
        // TODO: добавь оплату
        return true;
    }


}
