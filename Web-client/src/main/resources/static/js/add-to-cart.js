document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".buy-button").forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            let form = this.closest("form");
            let productId = form.querySelector("input[name='productId']").value;
            let quantity = form.querySelector("input[name='quantity']").value;

            fetch("/pk8000/catalog/api/add-to-cart", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ productId, quantity })
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        showNotification(`Добавлено: ${data.productName} x${data.quantity} (Сумма: $${data.totalPrice})`);
                        updateCartCount(data.cartTotalItems);                           // Обновляем значок корзины
                    } else {
                        showNotification(`Ошибка: ${data.error}`, true);
                    }
                })
                .catch(error => {
                    showNotification("Ошибка при добавлении в корзину", true);
                    console.error("Ошибка запроса:", error);
                });
        });
    });
});

function updateCartCount(newCount) {
    let cartCountElement = document.querySelector(".cart-count");
    if (cartCountElement) {
        cartCountElement.textContent = newCount;
    }
}

function showNotification(message, isError = false) {
    let notification = document.createElement("div");
    notification.className = "notification" + (isError ? " error" : "");
    notification.textContent = message;
    document.body.appendChild(notification);
    setTimeout(() => notification.remove(), 3000);
}
