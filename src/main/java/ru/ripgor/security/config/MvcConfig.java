package ru.ripgor.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Интерфейс WebMvcConfigurer позволяет писать меньше кода в случаях,
 * когда нам необходимо вернуть только представление без какой-либо внутренней логики.
 * В этом случае можно не писать дополнительные методы в контроллерах, а просто прописать их здесь
 */

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("hello");
    }

}
