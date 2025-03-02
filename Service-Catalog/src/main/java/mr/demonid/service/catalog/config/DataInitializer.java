package mr.demonid.service.catalog.config;

import mr.demonid.service.catalog.services.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Создание категорий.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryService categoryService;

    public DataInitializer(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) {
//        categoryService.createCategory("Logic");
//        categoryService.createCategory("Action");
//        categoryService.createCategory("Race");
//        categoryService.createCategory("Other");
    }
}