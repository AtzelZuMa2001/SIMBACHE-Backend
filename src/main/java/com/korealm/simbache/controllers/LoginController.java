package com.korealm.simbache.controllers;

import com.korealm.simbache.dtos.login.LoginRequest;
import com.korealm.simbache.dtos.login.LoginResponse;
import com.korealm.simbache.services.LoginServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
* Esto es un controlador, el punto de entrada de los clientes hacia nuestro backend.
* Nuestro controlador recibe la solicitud o "request" (correctamente se les llama requests), y devuelve la respuesta o "response".
* Cada función declarada en esta clase se considera un punto de entrada, o en inglés, un "endpoint".
* Cada endpoint puede recibir diferentes tipos de datos, como JSON, XML, etc, pero nosotros solo vamos a recibir JSON.
*
* Esto es importante, y estricto: Un endpoint debe hacer 1 sola cosa, pero la debe hacer bien. 1 Sola responsabilidad.
* Por ejemplo, no deberíamos en un endpoint hacer 2 cosas diferentes, como obtener un usuario y actualizarlo.
* Si no que un endpoint obtiene el usuario y otro actualiza el usuario.
* El frontend es el que debe saber qué hacer de su lado, no el backend.
*
* Pero bueno, continuando:
* En Spring un @Controller devuelve vistas, o sea, un HTML o un XML o algo visual, pues.
* Por otro lado, un @RestController no devuelve una vista, sino que devuelve datos puros: devuelve JSON, XML, etc.
* Nosotros no vamos a usar Spring para generar ni devolver vistas, sino que vamos a usar Spring para administrar nuestros datos,
* y por lo tanto, solo vamos a trabajar con @RestController.
* */

/*
*
 * Esto es un controlador REST, que es una especie de interfaz que nos permite hacer llamadas a nuestro servicio cuando alguien de afuera hace una petición, y devolver datos o información al cliente, como dije antes.
 * Esto es muy útil cuando queremos que nuestro servicio sea accesible desde cualquier lugar, desde un navegador, una compu, iOS, Android, etc.
 *
 * @RequestMapping le dice a spring que este controlador va a tener todos sus métodos/endpoints adentro de la ruta /api/auth.
* */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {
    // Declaramos todos los servicios que necesitamos en este controlador.
    // Recordá que Spring crea las instancias de los servicios para nosotros. No tenemos que hacerlo, más que solo declararlos.
    private final LoginServiceImpl loginService;


    /* Importante:
        Vamos a ver que nuestros métodos devuelven un objeto de tipo ResponseEntity. Esto es un objeto de Spring que nos permite devolver datos de manera fácil y rápida, porque encapsula la información que queremos devolver + el código de estado HTTP que queremos devolver. En este caso le decimos que responda un código HTTP 200 (o sea, todo OK) y que devuelva un objeto LoginResponse que creamos nosotros.
    * */

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // Aquí llamamos al servicio que nos va a manejar la lógica de login.
        // Si todo sale bien, el servicio nos devuelve un objeto LoginResponse que contiene el token de autenticación, y el usuario.
        // Si algo sale mal, en el propio servicio se lanzará una excepción, y Spring nos devolverá un código HTTP 401 (Unauthorized) con un mensaje de error.
        LoginResponse response = loginService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-Auth-Token") String token) {
        loginService.logout(token);
        return ResponseEntity.ok().build(); // HTTP 200 OK, sin contenido. No necesitamos devolver nada más que el OK.
    }
}
