package com.korealm.simbache.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* Este archivo permite configurar las peticiones CORS. En palabras sencillas, nos permite configurar QUIÉN puede hacer las peticiones.
* Aquí podemos incluir una lista de IPs o dominios que pueden hacer peticiones, y a todos los demás, el backend les devuelve un error 401 (Unauthorized).
* */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:31235")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*");
    }
}
