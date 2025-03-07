package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.CartItemRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CART-SERVICE", configuration = FeignClientConfig.class)      // имя сервиса, под которым он зарегистрирован в Eureka
public interface CartServiceClient {

    @PostMapping("/pk8000/api/public/cart//add")
    ResponseEntity<CartItemRequest> addItem(@RequestParam Long productId, @RequestParam Integer quantity);

}
