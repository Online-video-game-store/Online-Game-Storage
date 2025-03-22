package mr.demonid.service.order.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.order.config.NotificationWebSocketHandler;
import mr.demonid.service.order.dto.OrderCreatedEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pk8000/api/order")
@AllArgsConstructor
@Log4j2
public class ApiController {

    private StreamBridge streamBridge;

    private NotificationWebSocketHandler webSocketHandler;


    @GetMapping("/test-rabbit")
    public String testRabbit() {
        OrderCreatedEvent event = new OrderCreatedEvent("1234-5678-9876-0101", "userID()", List.of(), 12345.6789);
        log.info("Sending order created event: {}", event);
        streamBridge.send("orderCreate-out-0", event);
        webSocketHandler.sendMessageToAllClients("Заказ создан: " + event);
        return "test-rabbit";
    }
}
