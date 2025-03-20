package mr.demonid.service.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import mr.demonid.service.payment.dto.NewCardRequest;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cardNumber;

    private String expiryDate;
    private String cvv;

    @ManyToOne
    @JoinColumn(name = "user_payment_id")
    private UserPayment userPayment;


    /*
        Возврат номера карты, частично скрытого маской
     */
    // TODO: доделай!!!
    public String getSafeCardNumber() {
        return cardNumber;
    }

    /*
        Обеспечиваем уникальность для каждого объекта.
        А поскольку номер карты уникален, то учитываем только его.
    */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;
        return cardNumber.equals(card.cardNumber);
    }

    @Override
    public int hashCode() {
        return cardNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", cvv='" + cvv + '\'' +
                ", userPayment=" + (userPayment == null ? "[]" : userPayment.getUserId()) +
                '}';
    }
}
