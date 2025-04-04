package mr.demonid.web.client.links;

import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.ProduceFilter;
import mr.demonid.web.client.dto.ProductTableResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@FeignClient(name = "CATALOG-SERVICE", configuration = FeignClientConfig.class)      // имя сервиса, под которым он зарегистрирован в Eureka
public interface ProductServiceClient {

    @PostMapping("/pk8000/api/catalog/get-all-without-empty")
    ResponseEntity<PageDTO<ProductDTO>> getAllProductsWithoutEmpty(@RequestBody ProduceFilter filter, Pageable pageable);

    @PostMapping("/pk8000/api/catalog/get-all")
    ResponseEntity<PageDTO<ProductTableResponse>> getAllProducts(@RequestBody ProduceFilter filter, Pageable pageable);

    @GetMapping("/pk8000/api/catalog/get-product/{id}")
    ResponseEntity<ProductDTO> getProductById(@PathVariable Long id);

    @GetMapping("/pk8000/api/catalog/get-categories")
    ResponseEntity<List<ProductCategoryDTO>> getAllCategories();


    @PostMapping(value = "/pk8000/api/catalog-edit/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> handleUpload(@RequestPart("file") MultipartFile file);

}
