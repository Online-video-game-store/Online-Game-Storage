package mr.demonid.web.client.links;

import jakarta.servlet.http.HttpServletRequest;
import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.CartItemRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "CART-SERVICE", configuration = FeignClientConfig.class)      // имя сервиса, под которым он зарегистрирован в Eureka
public interface CartServiceClient {

    @PostMapping("/pk8000/api/public/cart/add")
    ResponseEntity<CartItemRequest> addItem(@RequestParam Long productId, @RequestParam Integer quantity);

    @GetMapping("/pk8000/api/public/cart/read")
    ResponseEntity<List<CartItemRequest>> getItems();

    @GetMapping("/pk8000/api/public/cart/count")
    ResponseEntity<Integer> getItemQuantity();

    @GetMapping("/pk8000/api/public/cart/clear")
    ResponseEntity<Void> clearCart();

    @PostMapping("/pk8000/api/public/cart/remove")
    ResponseEntity<Void> removeItem(@RequestParam Long productId);

    @PostMapping("/pk8000/api/public/cart/register-user")
    ResponseEntity<Void> registerUser(@RequestParam String anonId, @RequestParam String userId);
}
