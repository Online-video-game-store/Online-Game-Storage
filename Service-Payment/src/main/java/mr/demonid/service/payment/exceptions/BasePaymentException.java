package mr.demonid.service.payment.exceptions;

import lombok.Getter;


/**
 * Базовый класс эксепшенов.
 */
@Getter
public abstract class BasePaymentException extends RuntimeException {
    private final String title;

    public BasePaymentException(String title, String message) {
        super(message);
        this.title = title;
    }

}
