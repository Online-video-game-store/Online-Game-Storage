package mr.demonid.web.client.links;

import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.ProductFilter;
import mr.demonid.web.client.dto.ProductRequest;
import mr.demonid.web.client.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@FeignClient(name = "CATALOG-SERVICE", configuration = FeignClientConfig.class)      // имя сервиса, под которым он зарегистрирован в Eureka
public interface ProductServiceClient {

    @PostMapping("/pk8000/api/catalog/products/get-all")
    ResponseEntity<PageDTO<ProductResponse>> getAllProductsWithoutEmpty(@RequestBody ProductFilter filter, Pageable pageable);

    @GetMapping("/pk8000/api/catalog/products/get-product/{id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable Long id);

    @GetMapping("/pk8000/api/catalog/products/get-categories")
    ResponseEntity<List<ProductCategoryDTO>> getAllCategories();


    // админка
    @PostMapping("/pk8000/api/catalog/edit/get-all")
    ResponseEntity<PageDTO<ProductResponse>> getAllProducts(@RequestBody ProductFilter filter, Pageable pageable);

    @PostMapping("/pk8000/api/catalog/edit/update")
    ResponseEntity<Boolean> updateProduct(@RequestBody ProductRequest product);

    @PostMapping(value = "/pk8000/api/catalog/edit/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Boolean> uploadImage(@PathVariable Long id,
                                       @RequestPart("file") MultipartFile file,
                                       @RequestParam(value = "replace", required = false) String replaceFileName);

    @DeleteMapping("/pk8000/api/catalog/edit/{id}/delete")
    ResponseEntity<Boolean> deleteImage(@PathVariable Long id, @RequestParam String fileName);

}
