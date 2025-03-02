package mr.demonid.gateway.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private AppConfig appConfig;

// TODO: после тестирования убрать путь "/pk8000/api/catalog/**" !!!

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        var logoutHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        logoutHandler.setPostLogoutRedirectUri(appConfig.getLogoutUri());

        http.authorizeExchange(auth -> auth
                .pathMatchers("/pk8000/catalog/**", "/pk8000/auth/**", "/login", "/logout", "/pk8000/api/catalog/**").permitAll()
                .anyExchange().authenticated())
                // Настраиваем аутентификацию
                .oauth2Login(oauth2Login -> oauth2Login
                        .authenticationSuccessHandler((webFilterExchange, authentication) -> {
                            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
                            response.setStatusCode(HttpStatus.FOUND);  // Статус для редиректа
                            // Указываем URL, куда перейдем после успешной аутентификации
                            response.getHeaders().setLocation(URI.create(appConfig.getLoginUri()));
                            return Mono.empty();
                        }))
                // Настраиваем выход из профиля
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL для вызова logout
                        .logoutSuccessHandler(logoutHandler)
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }


}
