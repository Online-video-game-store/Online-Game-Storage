package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;


/**
 * Запрос на изменение данных (из JS в контроллер)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTableRequest {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Long category;
    private int stock;
    private String description;
    private String imageFileName;
    private MultipartFile file;
}
