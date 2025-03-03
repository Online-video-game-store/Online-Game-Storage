package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.store.commons.dto.ProductCategoryDTO;
import mr.demonid.store.commons.dto.ProductDTO;
import mr.demonid.web.client.links.ProductServiceClient;
import mr.demonid.web.client.services.ProductServices;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog")
public class WebController {

    private ProductServices productServices;


    @GetMapping("/index")
    public String index(Model model) {

        log.info("-- index page");

        boolean isAuthenticated = IdnUtil.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("username", isAuthenticated ? IdnUtil.getUserName() : null);

        List<ProductCategoryDTO> categories = productServices.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", categories.get(0).getName());

        Pageable pageable = PageRequest.of(0, 8, Sort.by("id").ascending());
        List<ProductDTO> products = productServices.getAllProducts(null, pageable).getContent();

        model.addAttribute("products", products);

        model.addAttribute("cartItemCount", 0);

        return "home";
    }
}
