package mr.demonid.web.client.services;

import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.dto.ProduceFilter;
import mr.demonid.web.client.dto.ProductTableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductServices {
    PageDTO<ProductDTO> getProductsWithoutEmpty(ProduceFilter filter, Pageable pageable);
    PageDTO<ProductTableResponse> getAllProducts(ProduceFilter filter, Pageable pageable);
    ProductDTO getProductById(Long id);
    List<ProductCategoryDTO> getAllCategories();

    void updateImage(MultipartFile file);
}
