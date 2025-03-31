package mr.demonid.web.client.configs;

import lombok.extern.slf4j.Slf4j;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("Authorization");
        UUID userId = IdnUtil.getUserId();
        log.info("--- Interceptor: inject userId = {}", userId);
        if (userId != null) {
            attributes.put("userId", userId);
            return true;
        }

//        Jwt jwt = jwtDecoder.decode(token.replace("Bearer ", ""));
//        // Декодируем userId из JWT-токена
//        Object JwtUtils;
//        String userId = JwtUtils.extractUserId(token);
//        if (userId != null) {
//            attributes.put("userId", userId);
//            return true;
//        }

        return false; // Отклоняем соединение, если userId не найден
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, 
                               WebSocketHandler wsHandler, Exception exception) {
        // После handshake ничего не делаем
    }
}
