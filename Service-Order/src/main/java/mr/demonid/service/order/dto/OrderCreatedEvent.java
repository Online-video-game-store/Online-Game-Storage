package mr.demonid.service.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private List<String> productIds;
    private double totalAmount;

    public OrderCreatedEvent(String orderId, String userId, List<String> productIds, double totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.productIds = productIds;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", productIds=" + productIds +
                ", totalAmount=" + totalAmount +
                '}';
    }
}