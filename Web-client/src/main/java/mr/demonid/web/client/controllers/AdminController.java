package mr.demonid.web.client.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.ProductRequest;
import mr.demonid.web.client.dto.ProductResponse;
import mr.demonid.web.client.services.ProductServices;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog/api/product")
public class AdminController {

    private ProductServices productServices;


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProduct(@ModelAttribute ProductRequest product) throws IOException {
        System.out.println("save(): " + product);

        if (productServices.updateProduct(product)) {
            return ResponseEntity.ok().build();
        }
//        if (product.getFile() != null && !product.getFile().isEmpty()) {
//            System.out.println("-- new file: " + product.getFile().getOriginalFilename());
//            productServices.updateImage(product.getFile());
//        }
//        System.out.println("original file: " + product.getImageFileName());

        return ResponseEntity.badRequest().build();
    }


    /**
     * Получение полого описания продукта.
     * Возвращает список картинок в Base64.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @GetMapping("/{id}")
    public ResponseEntity<List<String>> listImages(@PathVariable Long id) {
        System.out.println("get product: " + id);
        ProductResponse res = productServices.getProductById(id);
        System.out.println("product: " + res);
        if (res == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(res.getImageUrls());
    }


    /**
     * Загрузка на сервер нового изображения, или замена старому.
     * @param id              Продукт
     * @param file            Новый файл.
     * @param replaceFileName Имя старого файла, или null (если просто добавляем новый файл)
     * @return                Строку "Uploaded", если все прошло успешно.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadImage(@PathVariable Long id,
                                              @RequestPart("file") MultipartFile file,
                                              @RequestParam(value = "replace", required = false) String replaceFileName) {
        System.out.println("upload product: " + id + ", " + file.getOriginalFilename() + ", replace to: " + replaceFileName);
        if (productServices.uploadImage(id, file, replaceFileName)) {
            return ResponseEntity.ok("Uploaded");
        }
        return ResponseEntity.noContent().build();
    }


    /**
     * Удаление изображения.
     * @param productId Продукт
     * @param fileName  Имя удаляемого файла.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @DeleteMapping("{productId}/image/{fileName}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long productId,
            @PathVariable String fileName) {

        System.out.println("delete product: " + productId + ", " + fileName);
        productServices.deleteImage(productId, fileName);
        return ResponseEntity.noContent().build();
    }

}
