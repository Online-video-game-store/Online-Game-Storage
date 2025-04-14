package mr.demonid.web.client.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private AnonymousCookieFilter anonymousCookieFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Разрешаем CORS
                .csrf(AbstractHttpConfigurer::disable)                      // Отключаем CSRF для запросов API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/pk8000/catalog/**", "/pk8000/auth/**", "/pk8000/catalog/images/**", "/ws/**").permitAll()  // Главная и публичные ресурсы
                        .anyRequest().authenticated()  // Остальные требуют аутентификации
                )
//                .anonymous(Customizer.withDefaults()) // Включение анонимных пользователей
                .addFilterAfter(anonymousCookieFilter, SecurityContextHolderFilter.class)
//                .addFilterBefore(anonymousCookieFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jt -> jt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .formLogin(AbstractHttpConfigurer::disable)    // Отключаем перенаправление на форму входа
                .httpBasic(AbstractHttpConfigurer::disable);   // Отключаем Basic Auth

        return http.build();
    }

    /**
     * Конвертер под Keycloak-токены.
     * Извлекает из полей запроса значения ROLE и SCOPE.
     * Задает новое поля с именем пользователя, поскольку в Keycloak
     * оно по умолчанию в поле "name", а в "sub" хранится идентификатор пользователя.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        converter.setPrincipalClaimName("name");
        return converter;
    }


}
