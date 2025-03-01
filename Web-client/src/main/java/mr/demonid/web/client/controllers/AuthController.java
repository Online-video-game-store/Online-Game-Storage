package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@AllArgsConstructor
@RequestMapping("/pk8000")
public class AuthController {


//    @GetMapping("/login")
//    public String login(Model model) {
//        log.info("-- login");
//        return "redirect:/pk8000/catalog/index";
//    }

    @GetMapping("logout")
    public String logout(Model model) {
        log.info("-- logout");
        return "redirect:/pk8000/catalog/index";
    }

}
