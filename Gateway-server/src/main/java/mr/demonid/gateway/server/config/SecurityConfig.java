package mr.demonid.gateway.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(auth -> auth.anyExchange().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }


//    /**
//     * Настройка потока безопасности (цепочки фильтров). Обрабатывает все входящие запросы.
//     * @param http Объект, предоставляющий API для настройки безопасности.
//     */
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .authorizeExchange(exchange -> exchange
//                        .anyExchange().authenticated()      // все запросы требуют аутентификации
//                )
//                // настройка переадресации на форму входа OAuth2
//                .oauth2Login(Customizer.withDefaults())
//                // настраиваем обработку OAuth2-токенов, в данном случае Jwt
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt
//                                .jwtAuthenticationConverter(reactiveJwtAuthenticationConverter())
//                        )
//                )
//                .csrf(ServerHttpSecurity.CsrfSpec::disable);    // отключаем для всех запросов
//        return http.build();
//    }
//
//    /**
//     * Извлекает из токена список привилегий и возвращает их в виде объекта GrantedAuthority.
//     * Если дошли сюда, то токен уже проверен на подлинность через JWK или секретный ключ.
//     *
//     * После этого метода роли добавляются в SecurityContext и их можно использовать
//     * для проверки авторизации, например через аннотации:
//     * '@PreAuthorize("hasRole('USER')")'
//     * '@GetMapping("/user")' ... или на уровне фильтров доступных адресов.
//     */
//    private Converter<Jwt, Mono<AbstractAuthenticationToken>> reactiveJwtAuthenticationConverter() {
//        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_");       // префикс для прав
//        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");   // поле, содержащее scope
//
//        return jwt -> {
//            // получаем authorities из scope
//            Collection<GrantedAuthority> authorities = new HashSet<>(grantedAuthoritiesConverter.convert(jwt));
//            // добавляем роли
//            List<String> roles = jwt.getClaimAsStringList("authorities");
//            if (roles != null) {
//                roles.forEach(role -> {
//                    if (role.startsWith("ROLE_")) {
//                        authorities.add(new SimpleGrantedAuthority(role));
//                    } else {
//                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//                    }
//                });
//            }
//            // формируем токен на основе собранных authorities
//            AbstractAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt, authorities);
//            return Mono.just(authenticationToken);
//        };
//    }

}
