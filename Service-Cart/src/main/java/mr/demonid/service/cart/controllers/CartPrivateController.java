package mr.demonid.service.cart.controllers;

import lombok.extern.slf4j.Slf4j;
import mr.demonid.service.cart.services.CartFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/pk8000/api/private/cart")
public class CartPrivateController {

    @Autowired
    private CartFactory cartFactory;

    /**
     * Регистрирует пользователя.
     * @param anonId Идентификатор пользователя до авторизации.
     * @param userId Текущий идентификатор пользователя.
     */
    @PostMapping("/register-user")
    public ResponseEntity<Void> registerUser(@RequestParam String anonId, @RequestParam String userId) {
        log.info("-- Registering anon {} to user with id {}", anonId, userId);
//        cartFactory.registerUser(anonId, userId);
        return ResponseEntity.ok().build();
    }

}
