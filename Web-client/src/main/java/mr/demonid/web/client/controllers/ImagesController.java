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
@RequestMapping("/pk8000/catalog/api/product")
public class ImagesController {

    private ProductServices productServices;


    /**
     * Получение полого описания продукта.
     * Возвращает список картинок в Base64.
     */
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
    @DeleteMapping("{productId}/image/{fileName}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long productId,
            @PathVariable String fileName) {

        System.out.println("delete product: " + productId + ", " + fileName);
        productServices.deleteImage(productId, fileName);
        return ResponseEntity.noContent().build();
    }

}
