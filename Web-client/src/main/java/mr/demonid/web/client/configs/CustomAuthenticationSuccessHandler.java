package mr.demonid.web.client.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Перенос данных анонимного пользователя в его авторизированный
 * профиль.
 */
@Component
@Log4j2
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String COOKIE_NAME = "ANON_ID";

//    CartService cartService;
//
//    public CustomAuthenticationSuccessHandler(CartService cartService) {
//        this.cartService = cartService;
//    }

    /**
     * Этот фильтр вызывается если аутентификация пользователя прошла успешно,
     * можем сделать что-то с его данными (если нужно). Или
     * просто разослать извещение всем заинтересованным сервисам.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Cookie anonCookie = IdnUtil.getCookie(COOKIE_NAME, request.getCookies());
        if (anonCookie != null) {
            // Переносим данные из анонимного контекста в авторизованный
//            cartService.transferAnonToUser(anonCookie.getValue(), IdnUtil.getUserId());

            log.info("-- SimpleUrlAuthenticationSuccessHandler: copy datas of cart to registered user!");
            // Удаляем куки после успешной авторизации
            anonCookie.setMaxAge(0);
            anonCookie.setPath("/");
            response.addCookie(anonCookie);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}