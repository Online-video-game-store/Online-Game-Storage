package mr.demonid.web.client.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


/**
 * Данные о товаре в админку.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTableResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long category;
    private int stock;
    private String description;
    private List<String> imageFileNames;
}
