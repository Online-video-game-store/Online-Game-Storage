package mr.demonid.service.payment.repository;

import mr.demonid.service.payment.domain.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserPaymentRepository extends JpaRepository<UserPayment, UUID> {

}
