package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.dto.CartItemRequest;
import mr.demonid.web.client.links.CartServiceClient;
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
    private CartServiceClient cartServiceClient;

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest request) {
        log.info("-- add product to cart: {}, {} шт", request.getProductId(), request.getQuantity());

        ResponseEntity<CartItemRequest> res = cartServiceClient.addItem(request.getProductId(), request.getQuantity());
        if (res.getStatusCode().is2xxSuccessful()) {
            ProductDTO product = productServices.getProductById(request.getProductId());
            // Возвращаем JSON-ответ
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "productName", product.getName(),
                    "quantity", request.getQuantity(),
                    "totalPrice", product.getPrice().multiply(new BigDecimal(request.getQuantity()))
            ));
        }
        return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", ("Не удалось добавить товар в корзину. Ошибка " + res.getStatusCode())
        ));
    }

}
