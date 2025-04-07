package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.*;
import mr.demonid.web.client.services.CartServices;
import mr.demonid.web.client.services.ProductServices;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> addToCart(@RequestBody CartItemResponse request) {
        CartItemResponse res = cartServices.addItem(request.getProductId(), request.getQuantity());
        if (res != null) {
            return ResponseEntity.ok(Map.of("success", true, "message", ""));
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Не удалось добавить товар в корзину. Попробуйте позже."));
    }
//        return ResponseEntity.ok(new CartAddStatus(true, "success"));
//        return ResponseEntity.badRequest().body(new CartAddStatus(false, "Не удалось добавить товар в корзину. Попробуйте позже."));

    /**
     * Возвращает кол-во товаров в корзине.
     */
    @GetMapping("/get-cart-count")
    public ResponseEntity<Integer> getCartCount() {
        return ResponseEntity.ok(cartServices.getCountItems());
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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveProduct(@ModelAttribute ProductRequest product) throws IOException {
        System.out.println("save(): " + product);

//        if (product.getFile() != null && !product.getFile().isEmpty()) {
//            System.out.println("-- new file: " + product.getFile().getOriginalFilename());
//            productServices.updateImage(product.getFile());
//        }
//        System.out.println("original file: " + product.getImageFileName());


        return ResponseEntity.ok().build();
    }


}

