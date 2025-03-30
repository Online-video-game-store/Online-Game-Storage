package mr.demonid.service.catalog.events;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.dto.events.OrderCancelEvent;
import mr.demonid.service.catalog.services.ProcessService;
import mr.demonid.service.catalog.services.ReservedService;
import mr.demonid.service.catalog.services.tools.JwtService;
import mr.demonid.service.catalog.utils.TokenTool;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;


/**
 * Слушатель для канала orderCancel-in-0.
 * Все сообщения должны содержать в заголовке Jwt-токен,
 * который проверяется на сервере-аутентификации.
 */
@Component
@AllArgsConstructor
@Log4j2
public class CatalogCancelListener {

    private ProcessService processService;


    @Bean
    public Consumer<Message<Object>> channelOrderCancel() {
        return message -> processService.doProcess(message);
    }

}
