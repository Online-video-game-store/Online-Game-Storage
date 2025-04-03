package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.dto.ProduceFilter;
import mr.demonid.service.catalog.repositories.ProductRepository;
import mr.demonid.service.catalog.services.filters.ProductSpecification;
import mr.demonid.store.commons.dto.ProductDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

/**
 * Слой сервис по работе с БД товаров.
 */
@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Возвращает постраничный список товаров.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProducts(ProduceFilter produceFilter, boolean isIncludeEmpty, Pageable pageable) {
        Page<ProductEntity> items = productRepository.findAll(ProductSpecification.filter(produceFilter, isIncludeEmpty), pageable);
        return items.map(e -> new ProductDTO(e.getId(), e.getName(), e.getPrice(), e.getCategory().getId(), e.getStock(), e.getDescription(), encodeImageToBase64(e.getImageFile())));
    }

    /**
     * Возвращает информацию по конкретному товару.
     * @param productId Идентификатор товара.
     */
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long productId) {
         Optional<ProductEntity> opt = productRepository.findByIdWithCategory(productId);
         if (opt.isPresent()) {
             ProductEntity p = opt.get();
             return new ProductDTO(p.getId(), p.getName(), p.getPrice(), p.getCategory().getId(), p.getStock(), p.getDescription(), encodeImageToBase64(p.getImageFile()));
         }
         return null;
    }


    /**
     * Конвертирует файл в строку Base64.
     */
    private String encodeImageToBase64(String fileName) {
        try {
            // ClassPathResource строит путь из src/main/resource, независимо от того, упакован файл в JAR, или выполняется из IDEA.
            ClassPathResource imgFile = new ClassPathResource("pics/" + fileName);
            Path imagePath = imgFile.getFile().toPath();
            // Читаем байты и кодируем в Base64
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (IOException e) {
            return "";
        }
    }

}
