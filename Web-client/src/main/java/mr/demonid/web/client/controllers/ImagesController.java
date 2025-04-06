package mr.demonid.web.client.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.ProductResponse;
import mr.demonid.web.client.services.ProductServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog/api")
public class ImagesController {

    private ProductServices productServices;


    /**
     * Получение полого описания продукта.
     * Возвращает список картинок в Base64.
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<List<String>> listImages(@PathVariable Long id) {
        System.out.println("get product: " + id);
        ProductResponse res = productServices.getProductById(id);
        System.out.println("product: " + res);
        if (res == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.getImageUrls());
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<?> uploadImages(@PathVariable Long id,
                                          @RequestParam("images") List<MultipartFile> images) {

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/product/{productId}/image/{fileName}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long productId,
            @PathVariable String fileName) {

        return ResponseEntity.noContent().build();
    }

}
