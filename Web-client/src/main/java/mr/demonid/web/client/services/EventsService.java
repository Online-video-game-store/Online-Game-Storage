package mr.demonid.web.client.services;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.events.OrderDoneEvent;
import mr.demonid.web.client.dto.events.OrderFailEvent;
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
    private MessageMapper messageMapper;

    private WebSocketService webSocketService;
    private CartServices cartServices;


    public void doProcess(Message<Object> message) {
        try {
            String jwtToken = tokenTool.getCurrentToken(message);
            if (jwtToken != null && jwtService.createSecurityContextFromJwt(jwtToken)) {
                String eventType = (String) message.getHeaders().get("routingKey");

                if ("order.done".equals(eventType)) {
                    OrderDoneEvent event = messageMapper.map(message, OrderDoneEvent.class);
                    finishOrder(event);
                } else if ("order.fail".equals(eventType)) {
                    OrderFailEvent event = messageMapper.map(message, OrderFailEvent.class);
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
    private void finishOrder(OrderDoneEvent event)
    {
        // удаляем товары из корзины
        event.getProducts().forEach(product -> cartServices.removeItem(product.getProductId()));
        // информируем юзера
        UUID userId = IdnUtil.getUserId();
        webSocketService.sendMessage(userId, event.getMessage());
    }

    /*
    Заказ завершился ошибкой.
    */
    private void failOrder(OrderFailEvent event) {
        log.error("-- order {} cancelled", event);
        UUID userId = IdnUtil.getUserId();
        webSocketService.sendMessage(userId, "Ошибка формирования заказа: " + event.getMessage());
    }

}
