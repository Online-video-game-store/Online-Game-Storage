package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.store.commons.dto.PageDTO;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.services.CartServices;
import mr.demonid.web.client.services.ProductServices;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@Controller
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog")
public class WebController {

    private ProductServices productServices;
    private CartServices cartServices;


    @GetMapping("/index")
    public String index(
        @RequestParam(name = "elemsOfPage", defaultValue = "8") int pageSize,
        @RequestParam(name = "pageNo", defaultValue = "0") int currentPage,
        @RequestParam(name = "categoryId", defaultValue = "0") Long categoryId,
        Model model) {
        log.info("index. Current page = {}, categoryId = {}", currentPage, categoryId);

        boolean isAuthenticated = IdnUtil.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("username", isAuthenticated ? IdnUtil.getUserName() : null);

        List<ProductCategoryDTO> categories = productServices.getAllCategories();
        categories.add(0, new ProductCategoryDTO(0L, "All", "Все товары"));
        if (categoryId != null) {
            ProductCategoryDTO curCat = categories.stream().filter(e -> Objects.equals(e.getId(), categoryId)).findFirst().orElse(null);
            if (curCat != null) {
                model.addAttribute("currentCategory", curCat.getName());
            } else {
                model.addAttribute("currentCategory", "All");
            }
        } else {
            model.addAttribute("currentCategory", "All");
        }
        model.addAttribute("categories", categories);

        // Создаем выборку очередной страницы
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id").ascending());
        PageDTO<ProductDTO> page = productServices.getAllProducts(categoryId, pageable);
        model.addAttribute("products", page.getContent());

        // корректируем данные о страницах
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("elemsOfPage", pageSize);
        model.addAttribute("categoryId", categoryId);

        model.addAttribute("cartItemCount", cartServices.getCountItems());

        return "home";
    }

}
