package mr.demonid.service.payment.controllers;

import mr.demonid.service.payment.dto.OrderCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Configuration
public class PaymentEventListener {

    @Bean
    public Consumer<OrderCreatedEvent> orderCreate() {
        return event -> {
            System.out.println("-- Received message: " + event);
            // Логика обработки события
        };
    }
}