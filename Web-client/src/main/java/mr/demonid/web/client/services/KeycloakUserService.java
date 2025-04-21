package mr.demonid.web.client.services;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * Сервис для получения данных о пользователе.
 * Запросы кэшируются.
 */
@Service
public class KeycloakUserService {

    @Cacheable(value = "keycloak-users", key = "#userId")
    public UserRepresentation getUserRepresentation(UUID userId) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        return keycloak
                .realm("online-store-realm")
                .users()
                .get(userId.toString())
                .toRepresentation();
    }

    /**
     * Очистка кэша.
     */
    @CacheEvict(value = "keycloak-users", key = "#userId")
    public void evictCache(UUID userId) {
        // да собственно тут ничего и не нужно.
    }

}
