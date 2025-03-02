package mr.demonid.service.catalog.repositories;

import mr.demonid.service.catalog.domain.BlockedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Резервирование товаров.
 */
@Repository
public interface BlockedProductRepository extends JpaRepository<BlockedProductEntity, UUID> {

}
