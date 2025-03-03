package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.domain.BlockedProductEntity;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.exceptions.CatalogException;
import mr.demonid.service.catalog.exceptions.NotAvailableException;
import mr.demonid.service.catalog.exceptions.NotFoundException;
import mr.demonid.service.catalog.repositories.ProductRepository;
import mr.demonid.store.commons.dto.ProductReservationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Слой сервиса по резервированию товаров для последующей оплаты или отмены резерва.
 */
@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class ReservedService {
    private final ProductRepository productRepository;
    private final BlockedProductService blockedProductService;

    /**
     * Резервирование товара для совершения покупки.
     */
    @Transactional
    public void reserve(ProductReservationRequest request) throws CatalogException {
        ProductEntity productEntity = productRepository.findByIdWithCategory(request.getProductId()).orElse(null);
        if (productEntity == null) {
            log.error("Product {} not found", request.getProductId());
            throw new NotFoundException();
        }
        if (productEntity.getStock() < request.getQuantity()) {
            log.error("Not enough stock! Request = {}, in stock = {}", request.getQuantity(), productEntity.getStock());
            throw new NotAvailableException();
        }
        // резервируем товар
        productEntity.setStock(productEntity.getStock() - request.getQuantity());
        log.info("Reserved product: {}", productEntity);
        productRepository.save(productEntity);
        blockedProductService.lockProduct(request.getOrderId(), productEntity.getId(), request.getQuantity());
    }

    /**
     * Отмена резерва товара.
     */
    @Transactional
    public void cancelReserved(UUID orderId) {
        BlockedProductEntity blockedProductEntity = blockedProductService.proofOfPurchaseOrder(orderId);
        if (blockedProductEntity != null) {
            ProductEntity productEntity = productRepository.findByIdWithCategory(blockedProductEntity.getProductId()).orElse(null);
            if (productEntity != null) {
                // возвращаем товар на место
                productEntity.setStock(productEntity.getStock() + blockedProductEntity.getQuantity());
                productRepository.save(productEntity);
            }
        }
    }

    /**
     * Списание товара из резерва.
     */
    public void approvedReservation(UUID orderId) {
        BlockedProductEntity blockedProductEntity = blockedProductService.proofOfPurchaseOrder(orderId);
        if (blockedProductEntity != null) {
            // да собственно больше ничего и не нужно делать, разве что в историю отправить.
        }
    }


}
