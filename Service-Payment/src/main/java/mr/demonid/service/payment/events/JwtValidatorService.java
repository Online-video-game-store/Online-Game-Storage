package mr.demonid.service.payment.events;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

/**
 * Валидатор Jwt-токена для "ручной" проверки (в RabbitMQ)
 */
@Service
@Log4j2
public class JwtValidatorService {

    private final JwtDecoder jwtDecoder;


    public JwtValidatorService(@Value("${spring.security.oauth2.resourceserver.uri-base}") String issuerUri) {
        this.jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
    }

    // Метод для валидации токена
    public boolean validateJwt(String token) {
        try {
            jwtDecoder.decode(token.replace("Bearer ", ""));
            log.info("-- Jwt-token is valid.");
            return true;
        } catch (JwtException e) {
            log.error("Invalid Jwt-token: {}", e.getMessage());
            return false;
        }
    }
}
