package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.login.LoginRequest;
import com.korealm.simbache.dtos.login.LoginResponse;

/*
* Esta interfaz no es parte del diseño MVC, no es necesario que imites esta arquitectura si no querés. Pero deja te explico:
*
* Es una buena práctica al construir un backend, crear interfaces para nuestros Servicios. Recordemos que una interfaz es como
* un contrato muy estricto que dice que todo lo que debe hacer una clase. Por lo tanto, leyendo una interfaz ya tenemos una
* vista muy rápida y precisa de cuáles métodos y qué hace un servicio, sin tener que navegar todo el código.
*
* Es una forma de ser ordenados, y escribir código escalable, y de calidad.
* */

public interface LoginService {
    LoginResponse login(LoginRequest request);

    void logout(String tokenId);
}