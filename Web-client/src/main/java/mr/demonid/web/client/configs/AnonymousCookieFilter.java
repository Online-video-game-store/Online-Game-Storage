package mr.demonid.web.client.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Log4j2
public class AnonymousCookieFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "ANON_ID";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 дней

    /**
     * Вставляет во входящие запросы куки, идентифицирующие
     * анонимных пользователей.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Проверяем наличие cookie и если его нет, то создаем.
        if (IdnUtil.getCookie(COOKIE_NAME, request.getCookies()) == null) {
            String anonId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(COOKIE_NAME, anonId);
            cookie.setPath("/");
            cookie.setHttpOnly(true);   // защита от JavaScript атак
            cookie.setSecure(request.isSecure());
            cookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cookie);

            log.info("-- Cookie created: {}", cookie);
        } else {
            log.info("-- Cookie already exists: {}", IdnUtil.getCookie(COOKIE_NAME, request.getCookies()));
        }

        filterChain.doFilter(request, response);
    }

}
