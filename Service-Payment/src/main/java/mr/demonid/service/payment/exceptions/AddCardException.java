package mr.demonid.service.payment.exceptions;

public class AddCardException extends BasePaymentException {

    public AddCardException(String message) {
        super("Ошибка добавления новой карты", message);
    }

}
