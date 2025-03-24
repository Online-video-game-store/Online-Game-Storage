package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.domain.BlockedProductEntity;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.dto.CartItemResponse;
import mr.demonid.service.catalog.exceptions.CatalogException;
import mr.demonid.service.catalog.exceptions.NotAvailableException;
import mr.demonid.service.catalog.exceptions.NotFoundException;
import mr.demonid.service.catalog.repositories.ProductRepository;
import mr.demonid.store.commons.dto.ProductReservationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * Резервирование товаров для совершения покупки.
     * В случае отсутствия какого-либо товара, или недостаточного количества, бросит исключение.
     */
    @Transactional
    public void reserve(UUID orderId, List<CartItemResponse> items) throws CatalogException {
        items.forEach(item -> {
            ProductEntity productEntity = productRepository.findByIdWithCategory(item.getProductId()).orElse(null);
            if (productEntity == null) {
                log.error("Product with id {} not found", item.getProductId());
                throw new NotFoundException();
            }
            if (productEntity.getStock() < item.getQuantity()) {
                log.error("Not enough stock! Request = {}, in stock = {}", item.getQuantity(), productEntity.getStock());
                throw new NotAvailableException();
            }
            // резервируем товар
            productEntity.setStock(productEntity.getStock() - item.getQuantity());
            log.info("-- Зарезервирован товар: {}", productEntity);
            productRepository.save(productEntity);
            blockedProductService.lockProduct(orderId, productEntity.getId(), item.getQuantity());
        });
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
