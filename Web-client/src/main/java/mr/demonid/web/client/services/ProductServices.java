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
    List<ProductCategoryDTO> getAllCategories();
    ProductResponse getProductById(Long id);

    ResponseEntity<?> createProduct(ProductRequest product);
    ResponseEntity<?> updateProduct(ProductRequest product);
    ResponseEntity<?> deleteProduct(Long productId);
    ResponseEntity<?> uploadImage(Long productId, MultipartFile file, String replaceFileName);
    ResponseEntity<?> deleteImage(Long productId, String fileName);
}
