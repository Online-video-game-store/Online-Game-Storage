package mr.demonid.service.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPayment {
    @Id
    private UUID userId;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "userPayment", fetch = FetchType.EAGER)
    Set<Card> cards = new HashSet<>();

    public void addCard(Card card) {
        cards.add(card);
        card.setUserPayment(this);
    }
}
