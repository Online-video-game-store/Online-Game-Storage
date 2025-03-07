package mr.demonid.service.cart.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Цепочка фильтров безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Разрешаем CORS
                .csrf(AbstractHttpConfigurer::disable)                      // Отключаем CSRF для запросов API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/pk8000/api/public/cart/**").permitAll() // к корзине должен быть доступ вообще у всех
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()                       // Остальные требуют аутентификации
                )
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

//    /*
//     * Посмотрим цепочку фильтров, почему AnonymousUserFilter не получает управление.
//     */
//    @Bean
//    public ApplicationRunner logFilterChain(List<SecurityFilterChain> filterChains) {
//        System.out.println("-- all filters --");
//        return args -> filterChains.forEach(chain -> {
//            chain.getFilters().forEach(filter -> log.info("Filter in chain: {}", filter.getClass().getName()));
//        });
//    }
}

