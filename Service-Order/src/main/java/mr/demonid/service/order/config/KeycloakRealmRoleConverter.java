package mr.demonid.service.order.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;


@Log4j2
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        //        // извлекаем права
        getScopes(jwt, "scope").stream()
                .filter(Objects::nonNull)
                .map(scope -> scope.startsWith("SCOPE_") ? scope : "SCOPE_" + scope)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        // извлекаем роли
        getScopes(jwt, "roles").stream()
                .filter(Objects::nonNull)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        return authorities;
    }

    /**
     * Достает скопы из токена. Работает с двумя видами их представления:
     *  1) "scope": "openid write read delete email profile"
     *  2) "scope": ["openid", "write", "read", "delete", "email", "profile"]
     */
    private List<String> getScopes(Jwt jwt, String property) {
        if (jwt != null) {
            Object rawClaim = jwt.getClaim(property);

            if (rawClaim instanceof String str) {
                return List.of(str.split(" "));
            } else if (rawClaim instanceof Collection<?> list) {
                return list.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .toList();
            }
        }
        return List.of();
    }

}
