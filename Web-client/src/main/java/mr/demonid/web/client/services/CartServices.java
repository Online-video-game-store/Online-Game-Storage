package mr.demonid.web.client.services;

import mr.demonid.web.client.dto.CartItem;
import mr.demonid.web.client.dto.CartItemRequest;

import java.util.List;

public interface CartServices {

    /**
     * Добавление товара в корзину.
     */
    CartItemRequest addItem(Long productId, Integer quantity);

    /**
     * Список всех товаров в корзине текущего пользователя.
     */
    List<CartItem> getItems();

    /**
     * Удаление товара из корзины.
     */
    void removeItem(Long productId);

    /**
     * Полная очистка корзины.
     */
    void clearCart();

    /**
     * Количество всех товаров в корзине.
     */
    int getCountItems();

    /**
     * Аутентификация. Перевод из анонима в юзера.
     */
    boolean authUser(String anonId, String userId);

}
