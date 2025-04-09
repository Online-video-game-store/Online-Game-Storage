package mr.demonid.web.client.services;

import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.web.client.dto.ProductFilter;
import mr.demonid.web.client.dto.ProductRequest;
import mr.demonid.web.client.dto.ProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductServices {
    PageDTO<ProductResponse> getProductsWithoutEmpty(ProductFilter filter, Pageable pageable);
    PageDTO<ProductResponse> getAllProducts(ProductFilter filter, Pageable pageable);
    ProductResponse getProductById(Long id);
    List<ProductCategoryDTO> getAllCategories();

    ResponseEntity<?> createProduct(ProductRequest product);
    ResponseEntity<?> updateProduct(ProductRequest product);
    boolean uploadImage(Long productId, MultipartFile file, String replaceFileName);
    boolean deleteImage(Long productId, String fileName);
}
