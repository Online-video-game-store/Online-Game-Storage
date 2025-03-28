package mr.demonid.service.payment.utils;

import mr.demonid.service.payment.domain.Card;
import mr.demonid.service.payment.domain.PaymentLog;
import mr.demonid.service.payment.domain.PaymentMethod;
import mr.demonid.service.payment.domain.UserEntity;
import mr.demonid.service.payment.dto.CardResponse;
import mr.demonid.service.payment.dto.NewCardRequest;
import mr.demonid.service.payment.dto.PaymentLogResponse;
import mr.demonid.service.payment.dto.PaymentMethodResponse;


public class Converts {

    public static CardResponse cardToCardRequest(Card card) {
        return new CardResponse(card.getId(), card.getSafeCardNumber(), card.getExpiryDate());
    }

    public static Card newCardRequestToCard(NewCardRequest cardRequest, UserEntity userEntity) {
        return new Card(null, cardRequest.getCardNumber(), cardRequest.getExpiryDate(), cardRequest.getCvv(), userEntity);
    }

    public static PaymentLogResponse logToPaymentLogResponse(PaymentLog paymentLog) {
        return new PaymentLogResponse(
                paymentLog.getOrderId(),
                paymentLog.getUserId(),
                paymentLog.getPaymentMethodId(),
                paymentLog.getCardNumber(),
                paymentLog.getAmount(),
                paymentLog.getStatus(),
                paymentLog.getCreatedAt()
        );
    }

    public static PaymentMethodResponse paymentMethodToResponse(PaymentMethod method) {
        return new PaymentMethodResponse(method.getId(), method.getName(), method.isSupportsCards());
    }


}
