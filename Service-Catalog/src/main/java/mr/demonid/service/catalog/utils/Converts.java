package mr.demonid.service.catalog.utils;


import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.dto.ProductTableResponse;
import mr.demonid.service.catalog.dto.events.CatalogReserveRequestEvent;
import mr.demonid.service.catalog.dto.events.PaymentRequestEvent;
import mr.demonid.store.commons.dto.ProductDTO;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class Converts {

    public static PaymentRequestEvent createdToPayment(CatalogReserveRequestEvent event) {
        return new PaymentRequestEvent(
                event.getOrderId(),
                event.getUserId(),
                event.getPaymentId(),
                event.getCardId(),
                event.getTotalAmount()
        );
    }


    public static ProductDTO entityToDTO(ProductEntity entity) {
        String img = entity.getImageFiles().isEmpty() ? null : entity.getImageFiles().get(0);
        return new ProductDTO(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getCategory().getId(),
                entity.getStock(),
                entity.getDescription(),
                encodeImageToBase64(img)
        );
    }

    public static ProductResponse entityToProductResponse(ProductEntity entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getCategory().getId(),
                entity.getStock(),
                entity.getDescription(),
                entity.getImageFiles().stream().map(Converts::encodeImageToBase64).toList()
        );
    }

    public static ProductTableResponse entityToProductTableResponse(ProductEntity entity) {
        return new ProductTableResponse(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getCategory().getId(),
                entity.getStock(),
                entity.getDescription(),
                entity.getImageFiles()
        );
    }

    /**
     * Конвертирует файл в строку Base64.
     */
    private static String encodeImageToBase64(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            try {
                // ClassPathResource строит путь из src/main/resource, независимо от того, упакован файл в JAR, или выполняется из IDEA.
                ClassPathResource imgFile = new ClassPathResource("pics/" + fileName);
                Path imagePath = imgFile.getFile().toPath();
                // Читаем байты и кодируем в Base64
                byte[] imageBytes = Files.readAllBytes(imagePath);
                return Base64.getEncoder().encodeToString(imageBytes);

            } catch (IOException ignored) {
            }
        }
        return "";
    }

}
