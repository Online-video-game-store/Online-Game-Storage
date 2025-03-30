package mr.demonid.service.order.events;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.domain.OrderStatus;
import mr.demonid.service.order.services.EventsService;
import mr.demonid.service.order.services.OrderService;
import mr.demonid.service.order.services.tools.JwtService;
import mr.demonid.service.order.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;


/**
 * Слушатель для канала orderCancel-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Component
@AllArgsConstructor
@Log4j2
public class OrderCancelListener {

    private EventsService eventsService;


    @Bean
    public Consumer<Message<Object>> channelOrderCancel() {
        return message -> eventsService.doProcess(message);
    }

}
