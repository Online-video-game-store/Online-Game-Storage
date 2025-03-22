package mr.demonid.service.order.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.config.NotificationWebSocketHandler;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Log4j2
public class WebSocketServiceImpl implements WebSocketService {

    private NotificationWebSocketHandler webSocketHandler;


    /**
     * Рассылка сообщения по WebSocket всем подписчикам.
     * Самый удобный способ известить frontend о чем-то.
     */
    @Override
    public void sendMessage(String message) {
        webSocketHandler.sendMessageToAllClients(message);
    }

}
