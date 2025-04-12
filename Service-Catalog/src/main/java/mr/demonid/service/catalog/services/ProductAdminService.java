package mr.demonid.service.catalog.services;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mr.demonid.service.catalog.domain.ProductCategoryEntity;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.dto.ProduceFilter;
import mr.demonid.service.catalog.dto.ProductRequest;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.exceptions.CreateProductException;
import mr.demonid.service.catalog.exceptions.UpdateImageException;
import mr.demonid.service.catalog.exceptions.UpdateProductException;
import mr.demonid.service.catalog.repositories.CategoryRepository;
import mr.demonid.service.catalog.repositories.ProductRepository;
import mr.demonid.service.catalog.services.filters.ProductSpecification;
import mr.demonid.service.catalog.utils.Converts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductAdminService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Converts converts;

    @Value("${app.images-path}")
    private String imagesPath;
    @Value("${app.images-temp}")
    private String tempPath;

    /**
     * Возвращает постраничный список товаров для админки.
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(ProduceFilter produceFilter, Pageable pageable) {
        Page<ProductEntity> items = productRepository.findAll(ProductSpecification.filter(produceFilter, true), pageable);
        return items.map(converts::entityToProductResponse);
    }

    /**
     * Добавление нового товара.
     */
    @Transactional
    public void createProduct(ProductRequest product) {
        try {
            if (product == null || product.getCategory() == null) {
                throw new Exception("поступили некорректные данные");
            }
            product.setProductId(null);
            ProductCategoryEntity category = categoryRepository.findById(product.getCategory()).orElse(null);
            if (category == null) {
                throw new Exception("Неверная категория товара");
            }
            ProductEntity productEntity = converts.requestProductToEntity(product, category);
            productRepository.save(productEntity);
        } catch (Exception e) {
            throw new CreateProductException(e.getMessage());
        }
    }

    /**
     * Обновление данных о товаре.
     */
    @Transactional
    public void updateProduct(ProductRequest product) {
        try {
            if (product == null || product.getProductId() == null || product.getCategory() == null) {
                throw new Exception("поступили некорректные данные");
            }
            ProductCategoryEntity category = categoryRepository.findById(product.getCategory()).orElse(null);
            ProductEntity productEntity = productRepository.findById(product.getProductId()).orElse(null);
            if (productEntity == null || category == null) {
                throw new Exception("Данные о продукте или категории не найдены в БД");
            }
            productEntity.setName(product.getName());
            productEntity.setPrice(product.getPrice());
            productEntity.setStock(product.getStock());
            productEntity.setDescription(product.getDescription());
            productEntity.setCategory(category);
            productRepository.save(productEntity);
//            return converts.entityToProductResponse(productRepository.save(productEntity));
        } catch (Exception e) {
            throw new UpdateProductException(e.getMessage());
        }
    }

    public void updateImage(Long productId, MultipartFile file, String replaceFileName) {
        try {
            if (file.isEmpty()) {
                throw new Exception("Поступили некорректные данные");
            }
            ProductEntity productEntity = productRepository.findById(productId).orElse(null);
            if (productEntity == null) {
                throw new UpdateImageException("Товар не найден");
            }
            // проверяем, существует ли заменяемый файл
            if (replaceFileName != null && !replaceFileName.isEmpty()) {
                if (!productEntity.getImageFiles().contains(replaceFileName)) {
                    throw new UpdateImageException("Файл '" + replaceFileName + "' не найден");
                }
            }
            // сохраняем во временную папку
            Path tmpFile = toTempDirectory(file);
            // переносим в pics
            String finalFileName = replaceFileName == null ? file.getOriginalFilename() : replaceFileName.isBlank() ? file.getOriginalFilename() : replaceFileName;
            toImageDirectory(tmpFile, imagesPath, finalFileName);

            // корректируем БД
            if (replaceFileName == null || replaceFileName.isEmpty()) {
                productEntity.getImageFiles().add(finalFileName);
                productRepository.save(productEntity);
            }
        } catch (Exception e) {
            throw new UpdateImageException(e.getMessage());
        }
    }

    private void toImageDirectory(Path src, String destPath, String destFileName) {
        try {
            Path picsDir = Paths.get(destPath).toAbsolutePath().normalize();
            Files.createDirectories(picsDir);

            Path finalFile = picsDir.resolve(Objects.requireNonNull(destFileName));
            System.out.println("  -- replace: " + src.toFile() + " -> " + finalFile.toFile());
            Files.move(src, finalFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("-- file moved to: " + finalFile.toFile());

        } catch (Exception e) {
            throw new UpdateImageException(e.getMessage());
        }
    }

    private Path toTempDirectory(MultipartFile file) {
        // сохраняем во временную папку
        try {
            Path tmpDir = Paths.get(tempPath).toAbsolutePath().normalize();
            Files.createDirectories(tmpDir);

            Path tmpFile = tmpDir.resolve(UUID.randomUUID() + "_" + file.getOriginalFilename());
            System.out.println("  -- resolve: " + tmpFile.toFile());
            file.transferTo(tmpFile.toFile());

            // проверяем MIME-тип
            String contentType = Files.probeContentType(tmpFile);
            if (contentType == null || !contentType.startsWith("image/")) {
                // удаляем и возвращаем ошибку
                Files.deleteIfExists(tmpFile);
                throw new Exception("Файл '" + tmpFile.toFile() + "' не является изображением");
            }
            return tmpFile;
        } catch (Exception e) {
            throw new UpdateImageException(e.getMessage());
        }
    }



    /*
        System.out.println("-- receive file: " + file.getOriginalFilename());

        // сохраняем во временную папку
        Path tmpDir = Paths.get("uploads/tmp/").toAbsolutePath().normalize();
        Files.createDirectories(tmpDir);

        Path tmpFile = tmpDir.resolve(UUID.randomUUID() + "_" + file.getOriginalFilename());
        System.out.println("  -- resolve: " + tmpFile.toFile());
        file.transferTo(tmpFile.toFile());

        // проверяем MIME-тип
        String contentType = Files.probeContentType(tmpFile);
        if (contentType == null || !contentType.startsWith("image/")) {
            // удаляем и возвращаем ошибку
            Files.deleteIfExists(tmpFile);
            return ResponseEntity.badRequest().body("Файл не является изображением");
        }

        // переносим в pics
        Path picsDir = Paths.get("uploads/pics/").toAbsolutePath().normalize();
        Files.createDirectories(picsDir);

        // TODO: заменить на имя оригинального файла!!!
        Path finalFile = picsDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        Files.move(tmpFile, finalFile, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("-- file moved to: " + finalFile.toFile());
        return ResponseEntity.ok("Файл успешно загружен");




     */
}
