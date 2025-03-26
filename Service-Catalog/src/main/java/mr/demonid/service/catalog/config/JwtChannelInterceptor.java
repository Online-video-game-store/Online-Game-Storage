package mr.demonid.service.catalog.config;

import com.rabbitmq.client.LongString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.MessageRejectedException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;


/**
 * Фильтр для входящих сообщений от RabbitMQ (или аналогичных)
 */
@Component
@Log4j2
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();


    public JwtChannelInterceptor(@Value("${spring.security.oauth2.resourceserver.uri-base}") String issuerUri) {
        this.jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);
    }


    /**
     * Проверка Jwt-токена в сообщении и установка контекста безопасности на его основе.
     */
    @Override
    @Nullable
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        log.info("-- preSend() for message: {}", message);
        String token = extractToken(message);
        if (token == null) {
            log.warn("-- preSend() {}", "JWT token is null");
            return message;
//            throw new MessageRejectedException(message, "JWT token is null");
        }
        try {
            // создаем контекст безопасности для текущего потока
            Jwt jwt = jwtDecoder.decode(token);
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, converter.convert(jwt).getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            log.info("-- preSend() context: {}", context);
            return message;

        } catch (JwtValidationException ex) {
            log.error("-- preSend() exception: {}", ex.getMessage());
            throw new MessageRejectedException(message, "Invalid JWT: " + ex.getMessage());
        }
    }


    @Override
    @Nullable
    public void afterSendCompletion(@NonNull Message<?> message, @NonNull MessageChannel channel, boolean sent, Exception ex) {
        SecurityContextHolder.clearContext();
    }

    /*
     * Извлечение токена из заголовка сообщения.
     */
    @Nullable
    private String extractToken(@NonNull Message<?> message) {
        Object authHeader = message.getHeaders().get("Authorization");
        if (authHeader instanceof LongString longStringValue) {
            authHeader = longStringValue.toString();
        }
        if (authHeader instanceof String authHeaderStr && authHeaderStr.startsWith("Bearer ")) {
            return authHeaderStr.substring(7);
        }
        return null;
    }

}