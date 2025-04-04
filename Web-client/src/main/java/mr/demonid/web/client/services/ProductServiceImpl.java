package mr.demonid.web.client.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.dto.ProduceFilter;
import mr.demonid.web.client.dto.ProductTableResponse;
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
    public PageDTO<ProductDTO> getProductsWithoutEmpty(ProduceFilter filter, Pageable pageable) {
        log.info("getProducts: categoryId: {}, pageable: {}", filter, pageable);
        try {
            return productServiceClient.getAllProductsWithoutEmpty(filter, pageable).getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getProductsWithoutEmpty(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }

    @Override
    public PageDTO<ProductTableResponse> getAllProducts(ProduceFilter filter, Pageable pageable) {
        log.info("getAllProducts: categoryId: {}, pageable: {}", filter, pageable);
        try {
            return productServiceClient.getAllProducts(filter, pageable).getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getAllProducts(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }


    @Override
    public ProductDTO getProductById(Long id) {
        try {
            return productServiceClient.getProductById(id).getBody();
        } catch (FeignException e) {
            log.error("ProductServiceImpl.getProductById(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return new ProductDTO();
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
    public void updateImage(MultipartFile file) {
        try {
            productServiceClient.handleUpload(file);
        } catch (FeignException e) {
            log.error("ProductServiceImpl.updateImage(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }

}
