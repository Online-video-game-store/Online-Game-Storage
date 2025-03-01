package mr.demonid.web.client.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.ProductInfo;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000")
public class WebController {

    @GetMapping("/auth/login")
    public String login(@RequestHeader Map<String, String> headers) {
        log.info("-->> login");
        headers.forEach((k, v) -> log.info("  -- {}: {}", k, v));

        return "redirect:/pk8000/catalog/index";
    }

    @GetMapping("/auth/info-out")
    public String logout() {
        log.info("-->> logout");

        return "redirect:/pk8000/catalog/index";
    }


    @GetMapping("/catalog/index")
    public String index(Model model) {

        log.info("-- index page");

        boolean isAuthenticated = IdnUtil.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("username", isAuthenticated ? IdnUtil.getUserName() : null);

        model.addAttribute("categories", List.of("Гонки", "Приключения", "Квесты"));
        model.addAttribute("currentCategory", "All");

        List<ProductInfo> products = List.of(new ProductInfo(1L, "Асса", BigDecimal.ZERO, 12, "dsec", "image.png"));

        model.addAttribute("products", products);

        model.addAttribute("cartItemCount", 0);

        return "home";
    }
}
