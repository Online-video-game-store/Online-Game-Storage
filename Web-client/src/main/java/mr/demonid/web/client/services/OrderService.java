package mr.demonid.web.client.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.dto.CartItemResponse;
import mr.demonid.web.client.dto.PaymentRequest;
import mr.demonid.web.client.dto.payment.OrderCreateRequest;
import mr.demonid.web.client.links.OrderServiceClient;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class OrderService {

    private CartServices cartServices;
    private OrderServiceClient orderServiceClient;


    public boolean createOrder(PaymentRequest request) {
        log.info("-- Creating order with payment: {}", request);
        String userId = IdnUtil.getUserId();
        if (userId != null) {
            // получаем товары из корзины и вычисляем общую сумму покупки
            List<CartItem> items = cartServices.getItems();
            List<CartItemResponse> list = items.stream().map(e -> new CartItemResponse(e.getProductId(), e.getQuantity())).toList();
            BigDecimal totalAmount = items.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (!list.isEmpty()) {
                OrderCreateRequest order = new OrderCreateRequest(
                        userId,
                        request.getPaymentMethodId(),
                        request.getCardId(),
                        totalAmount,
                        list
                );
                log.info("-- Order created: {}", order);
                // отсылаем заказ
                try {
                    return Boolean.TRUE.equals(orderServiceClient.createOrder(order).getBody());
                } catch (FeignException e) {
                    log.error("OrderService.createOrder(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
                }
            }
        }
        return false;
    }
}
