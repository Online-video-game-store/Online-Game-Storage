package mr.demonid.web.client.services;

import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.web.client.dto.ProduceFilter;
import mr.demonid.web.client.dto.ProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductServices {
    PageDTO<ProductResponse> getProductsWithoutEmpty(ProduceFilter filter, Pageable pageable);
    PageDTO<ProductResponse> getAllProducts(ProduceFilter filter, Pageable pageable);
    ProductResponse getProductById(Long id);
    List<ProductCategoryDTO> getAllCategories();

    void updateImage(MultipartFile file);
}
