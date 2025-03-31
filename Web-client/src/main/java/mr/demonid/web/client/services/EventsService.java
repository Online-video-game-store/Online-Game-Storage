package mr.demonid.web.client.services;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.events.CatalogFailEvent;
import mr.demonid.web.client.services.tools.JwtService;
import mr.demonid.web.client.services.tools.MessageMapper;
import mr.demonid.web.client.utils.IdnUtil;
import mr.demonid.web.client.utils.TokenTool;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class EventsService {

    private JwtService jwtService;
    private TokenTool tokenTool;
    private OrderService orderService;
    private MessageMapper messageMapper;

    private WebSocketService webSocketService;


    public void doProcess(Message<Object> message) {
        try {
            String jwtToken = tokenTool.getCurrentToken(message);
            if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("routingKey");

                if ("order.done".equals(eventType)) {
                    UUID orderId = messageMapper.map(message, UUID.class);
                    finishOrder(orderId);
                } else if ("order.fail".equals(eventType)) {
                    CatalogFailEvent event = messageMapper.map(message, CatalogFailEvent.class);
                    failOrder(event);

                } else {
                    log.warn("Неизвестный тип события: {}", eventType);
                }

            } else {
                log.error("Недействительный Jwt-токен");
            }

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    /*
     * Успешное завершение заказа.
     */
    private void finishOrder(UUID orderId)
    {
        log.info("-- finish order: {}", orderId);
        UUID userId = IdnUtil.getUserId();
        webSocketService.sendMessage(userId, "Заказ передан в службу комплектации и доставки.");
    }

    /*
    Заказ завершился ошибкой.
    */
    private void failOrder(CatalogFailEvent event) {
        log.error("-- order {} cancelled", event);
        UUID userId = IdnUtil.getUserId();
        webSocketService.sendMessage(userId, "Ошибка формирования заказа: " + event.getMessage());
    }

}
