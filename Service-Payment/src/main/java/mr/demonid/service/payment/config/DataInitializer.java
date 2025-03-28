package mr.demonid.service.payment.config;

import lombok.AllArgsConstructor;
import mr.demonid.service.payment.dto.PaymentMethodResponse;
import mr.demonid.service.payment.services.PaymentMethodService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Создание категорий.
 */
@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private PaymentMethodService paymentMethodService;


    @Override
    public void run(String... args) {
        System.out.println("-- Make payment methods --");
        paymentMethodService.createPaymentMethod(new PaymentMethodResponse(0, "Visa/MasterCard", true));
        paymentMethodService.createPaymentMethod(new PaymentMethodResponse(0, "PayPal", false));
        paymentMethodService.createPaymentMethod(new PaymentMethodResponse(0, "SberPay", false));
    }

}