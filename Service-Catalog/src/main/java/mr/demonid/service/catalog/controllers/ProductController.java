package mr.demonid.service.catalog.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.service.catalog.dto.ProduceFilter;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.dto.ProductTableResponse;
import mr.demonid.service.catalog.services.CategoryService;
import mr.demonid.service.catalog.services.ProductService;
import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.store.commons.dto.ProductDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pk8000/api/catalog")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;
    private CategoryService categoryService;


    /**
     * Возвращает постраничный список доступных товаров.
     */
    @PostMapping("/get-all-without-empty")
    public ResponseEntity<PageDTO<ProductDTO>> getAllProductsWithoutEmpty(@RequestBody ProduceFilter filter, Pageable pageable) {
        return ResponseEntity.ok(new PageDTO<>(productService.getProductsWithoutEmpty(filter, pageable)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/get-all")
    public ResponseEntity<PageDTO<ProductTableResponse>> getAllProducts(@RequestBody ProduceFilter filter, Pageable pageable) {
        return ResponseEntity.ok(new PageDTO<>(productService.getAllProducts(filter, pageable)));
    }
// http://localhost:9100/pk8000/api/catalog/get-all?&page=0&size=10&sort=name,asc

    /**
     * Возвращает продукт по его ID.
     */
    @GetMapping("/get-product/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse dto = productService.getProductById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }


    /**
     * Возвращает список категорий
     */
    @GetMapping("/get-categories")
    public ResponseEntity<List<ProductCategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


}
