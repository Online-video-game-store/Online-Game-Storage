package mr.demonid.callmeservice.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsMap("realm_access"))
                .map(realmAccess -> realmAccess.get("roles"))
                .filter(List.class::isInstance)
                .map(rolesObject -> (List<?>) rolesObject)      // теперь мы знаем что это List<?>
                .orElse(List.of())
                .stream()
                .filter(role -> role instanceof String)         // берём только строки
                .map(role -> (String) role)                     // теперь смело приводим Object к String
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

//    @Override
//    public Collection<GrantedAuthority> convert(Jwt jwt) {
//        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
//
//        if (realmAccess == null || !realmAccess.containsKey("roles")) {
//            return List.of();
//        }
//
//        Object rolesObject = realmAccess.get("roles");
//
//        if (!(rolesObject instanceof List<?> rolesList)) {
//            return List.of();
//        }
//
//        return rolesList.stream()
//                .filter(String.class::isInstance)       // нам нужны только сроки
//                .map(String.class::cast)                // теперь смело приводим Object к String
//                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
}
