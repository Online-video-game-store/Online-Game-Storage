package mr.demonid.service.order.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * WebSocketHandler для отправки уведомлений.
 */
@Component
@Log4j2
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Сохраняем сессию при подключении клиента
        sessions.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Обработка входящих сообщений от клиента
        System.out.println("Received from client: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Удаляем сессию при отключении клиента
        sessions.remove(session);
    }

    public void sendMessageToAllClients(String message) {
        for (WebSocketSession session : sessions) {
            try {
                // Отправка сообщения всем клиентам
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("NotificationWebSocketHandler: {}", e.getMessage());
            }
        }
    }
}
