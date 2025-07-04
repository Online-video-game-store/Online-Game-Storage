package mr.demonid.web.client.utils;


import com.rabbitmq.client.LongString;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TokenTool {

    /**
     * Извлекает из заголовка Message токен.
     */
    public String getCurrentToken(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        Object headerValue = headers.get("Authorization");

        if (headerValue instanceof LongString longStringValue) {
            return longStringValue.toString();

        } else if (headerValue instanceof String stringValue) {
            return stringValue;
        } else {
            log.error("TokenTool.getToken(): Can't get Jwt-token");
            return "";
        }
    }

}
