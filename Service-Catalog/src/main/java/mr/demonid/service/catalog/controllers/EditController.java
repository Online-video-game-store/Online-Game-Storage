package mr.demonid.service.catalog.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/api/catalog/edit")
public class EditController {


    /**
     * Добавление нового или замена существующего изображения.
     * @param id              Продукт.
     * @param file            Новый файл.
     * @param replaceFileName Имя существующего файла или null.
     * @return
     */
    @PostMapping("/{id}/upload")
    public ResponseEntity<Boolean> uploadImage(@PathVariable Long id,
                                              @RequestPart("file") MultipartFile file,
                                              @RequestParam(value = "replace", required = false) String replaceFileName) {
        System.out.println("upload product: " + id + ", " + file.getOriginalFilename() + ", replace to: " + replaceFileName);

        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Boolean> deleteImage(@PathVariable Long id, @RequestParam String fileName) throws IOException {
        System.out.println("delete product: " + id + ", " + fileName);

        return ResponseEntity.ok(true);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/upload")
    public ResponseEntity<String> handleUpload(@RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Пустой файл");
        }

        System.out.println("-- receive file: " + file.getOriginalFilename());

        // сохраняем во временную папку
        Path tmpDir = Paths.get("uploads/tmp/").toAbsolutePath().normalize();
        Files.createDirectories(tmpDir);

        Path tmpFile = tmpDir.resolve(UUID.randomUUID() + "_" + file.getOriginalFilename());
        System.out.println("  -- resolve: " + tmpFile.toFile());
        file.transferTo(tmpFile.toFile());

        // проверяем MIME-тип
        String contentType = Files.probeContentType(tmpFile);
        if (contentType == null || !contentType.startsWith("image/")) {
            // удаляем и возвращаем ошибку
            Files.deleteIfExists(tmpFile);
            return ResponseEntity.badRequest().body("Файл не является изображением");
        }

        // переносим в pics
        Path picsDir = Paths.get("uploads/pics/").toAbsolutePath().normalize();
        Files.createDirectories(picsDir);

        // TODO: заменить на имя оригинального файла!!!
        Path finalFile = picsDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        Files.move(tmpFile, finalFile, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("-- file moved to: " + finalFile.toFile());
        return ResponseEntity.ok("Файл успешно загружен");
    }

}
