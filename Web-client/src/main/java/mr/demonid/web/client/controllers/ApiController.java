package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.dto.CartAddItemFailed;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.dto.CartItemRequest;
import mr.demonid.web.client.dto.CartAddItemSuccess;
import mr.demonid.web.client.services.CartServices;
import mr.demonid.web.client.services.ProductServices;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog/api")
public class ApiController {

    private ProductServices productServices;
    private CartServices cartServices;


    /**
     * Проверка аутентификации пользователя.
     */
    @GetMapping("/check")
    public ResponseEntity<Void> check() {
        if (IdnUtil.isAuthenticated()) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * Добавление товара в корзину
     */
    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest request) {
        CartItemRequest res = cartServices.addItem(request.getProductId(), request.getQuantity());
        if (res != null) {
            ProductDTO product = productServices.getProductById(request.getProductId());
            return ResponseEntity.ok(
                    new CartAddItemSuccess(true,
                            product.getName(),
                            request.getQuantity(),
                            product.getPrice().multiply(new BigDecimal(request.getQuantity())),
                            cartServices.getCountItems()
                            ));
        }
        return ResponseEntity.badRequest().body(new CartAddItemFailed(false, "Не удалось добавить товар в корзину. Попробуйте позже."));
    }


    /**
     * Возвращает список товаров в корзине.
     */
    @GetMapping("/get-cart-items")
    private ResponseEntity<?> getCartItems() {
        List<CartItem> res = cartServices.getItems();
        return ResponseEntity.ok(res);
    }


    /**
     * Удаляет товар из корзины по его ID.
     */
    @DeleteMapping("/remove-cart-item/{itemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long itemId) {
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }
        cartServices.removeItem(itemId);
        return ResponseEntity.ok().build();
    }
}
