package mr.demonid.web.client.services;

import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.dto.ProduceFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductServices {
    PageDTO<ProductDTO> getAllProducts(ProduceFilter filter, Pageable pageable);
    ProductDTO getProductById(Long id);
    List<ProductCategoryDTO> getAllCategories();

}
