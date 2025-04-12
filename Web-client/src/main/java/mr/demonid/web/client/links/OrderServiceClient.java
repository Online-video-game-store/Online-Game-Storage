package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.payment.OrderCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ORDER-SERVICE", configuration = FeignClientConfig.class)
public interface OrderServiceClient {

    @PostMapping("/pk8000/api/order/create-order")
    ResponseEntity<?> createOrder(@RequestBody OrderCreateRequest orderCreateRequest);
}
