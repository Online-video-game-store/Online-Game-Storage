package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.domain.BlockedProductEntity;
import mr.demonid.service.catalog.repositories.BlockedProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class BlockedProductService {

    private BlockedProductRepository blockedRepository;

    /*
        Блокируем товар до подтверждения покупки (или его отмены).
     */
    public void lockProduct(UUID orderId, long productId, int quantity) {
        log.info("Reserving blocked product {} for order {} with quantity {}", productId, orderId, quantity);
        BlockedProductEntity res = blockedRepository.save(new BlockedProductEntity(orderId, productId, quantity));
        log.info("  -- result: {}", res);
    }

    /*
        Подтверждение покупки.
     */
    public BlockedProductEntity proofOfPurchaseOrder(UUID orderId) {
        BlockedProductEntity blockedProductEntity = blockedRepository.findById(orderId).orElse(null);
        if (blockedProductEntity != null) {
            log.info("-- proof of purchase order {}", orderId);
            blockedRepository.deleteById(orderId);
        }
        return blockedProductEntity;
    }

}
