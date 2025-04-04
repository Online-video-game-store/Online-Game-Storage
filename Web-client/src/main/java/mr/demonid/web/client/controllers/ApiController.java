package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.dto.*;
import mr.demonid.web.client.services.CartServices;
import mr.demonid.web.client.services.ProductServices;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveProduct(@ModelAttribute ProductTableRequest product) throws IOException {
        System.out.println("save(): " + product);

        if (product.getFile() != null && !product.getFile().isEmpty()) {
            System.out.println("-- new file: " + product.getFile().getOriginalFilename());
            productServices.updateImage(product.getFile());
        }
        System.out.println("original file: " + product.getImageFileName());


        return ResponseEntity.ok().build();
    }


}

