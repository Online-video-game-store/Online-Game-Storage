package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.dto.CartItemRequest;
import mr.demonid.web.client.links.CartServiceClient;
import mr.demonid.web.client.services.CartServices;
import mr.demonid.web.client.services.ProductServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog/api")
public class ApiController {

    private ProductServices productServices;
    private CartServices cartServices;

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest request) {
        log.info("-- add product to cart: {}, {} шт", request.getProductId(), request.getQuantity());

        CartItemRequest res = cartServices.addItem(request.getProductId(), request.getQuantity());
        if (res != null) {
            ProductDTO product = productServices.getProductById(request.getProductId());
            int itemsCount = cartServices.getCountItems();
            // Возвращаем JSON-ответ
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "productName", product.getName(),
                    "quantity", request.getQuantity(),
                    "totalPrice", product.getPrice().multiply(new BigDecimal(request.getQuantity())),
                    "cartTotalItems", itemsCount
            ));
        }
        return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", ("Не удалось добавить товар в корзину. Попробуйте позже."))
        );
    }

}
