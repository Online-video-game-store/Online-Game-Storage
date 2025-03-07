package mr.demonid.web.client.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Перенаправляем запросы:
 * с /pk8000/catalog/static/
 * на /static в папке ресурсов.
 * Префикс /pk8000/catalog добавлен в шаблоны Thymeleaf, иначе
 * для Gateway придется маршрутизировать пути "/css/**" и "/js/**"
 * на текущий микросервис, а этого хочется избежать.
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pk8000/catalog/static/**")
                .addResourceLocations("classpath:/static/"); // Путь к статическим файлам в resources
    }
}

