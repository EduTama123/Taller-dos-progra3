package com.itsqmet.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                //ruta de la cua se admiten datos
                .allowedOrigins("http://localhost:4200")
                //admitir todos los metodos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                //admitiri cuyalquier tipo de contenido
                .allowedHeaders("*");
    }
}
