package mr.demonid.web.client.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.web.client.dto.ProductFilter;
import mr.demonid.web.client.dto.ProductRequest;
import mr.demonid.web.client.dto.ProductResponse;
import mr.demonid.web.client.links.ProductServiceClient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Реализация интерфейса ProductServices
 */
@Service
@AllArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductServices {

    ProductServiceClient productServiceClient;


    @Override
    public PageDTO<ProductResponse> getProductsWithoutEmpty(ProductFilter filter, Pageable pageable) {
        log.info("getProducts: categoryId: {}, pageable: {}", filter, pageable);
        try {
            return productServiceClient.getAllProductsWithoutEmpty(filter, pageable).getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getProductsWithoutEmpty(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }

    @Override
    public PageDTO<ProductResponse> getAllProducts(ProductFilter filter, Pageable pageable) {
        log.info("getAllProducts: categoryId: {}, pageable: {}", filter, pageable);
        try {
            return productServiceClient.getAllProducts(filter, pageable).getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getAllProducts(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }

    @Override
    public ProductResponse getProductById(Long id) {
        try {
            return productServiceClient.getProductById(id).getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getProductById(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return null;
        }
    }


    @Override
    public List<ProductCategoryDTO> getAllCategories() {
        try {
            return productServiceClient.getAllCategories().getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getAllCategories(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return List.of(new ProductCategoryDTO(0L, "All", "Все категории"));
        }
    }



    @Override
    public boolean updateProduct(ProductRequest product) {
        try {
            return Boolean.TRUE.equals(productServiceClient.updateProduct(product).getBody());
        } catch (FeignException e) {
            log.error("ProductServiceImpl.updateProduct(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return false;
    }

    @Override
    public boolean uploadImage(Long productId, MultipartFile file, String replaceFileName) {
        try {
            return Boolean.TRUE.equals(productServiceClient.uploadImage(productId, file, replaceFileName).getBody());
        } catch (FeignException e) {
            log.error("ProductServiceImpl.uploadImage(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return false;
    }

    @Override
    public boolean deleteImage(Long productId, String fileName) {
        try {
            return Boolean.TRUE.equals(productServiceClient.deleteImage(productId, fileName).getBody());
        } catch (FeignException e) {
            log.error("ProductServiceImpl.deleteImage(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return false;
    }


}
