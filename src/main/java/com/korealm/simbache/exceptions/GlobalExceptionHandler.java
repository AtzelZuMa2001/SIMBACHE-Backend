package com.korealm.simbache.exceptions;

import com.korealm.simbache.dtos.login.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
Este archivo centraliza todos los errores o excepciones que se producen en el sistema.
Ya no necesitamos usar los try catch en cada controlador, ya que Spring lo hace por nosotros gracias a este archivo.

Básicamente, cuando se produce una excepción, esta clase lanza su código y en automático le devuelve al cliente el mensaje
de error de manera legible, y no un Stack Trace bestial.

Cuando escribimos este código, por ejemplo:
        throw new InvalidLoginException("Invalid credentials");
El sistema se encarga de devolverle esto al cliente:
        {
              "message": "Invalid credentials"
        }


En esta clase, debemos crear un método para cada excepción que queremos manejar a lo largo y ancho del sistema.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    * Los métodos deben declararse con la anotación ExceptionHandler, y recibe como parametro la clase de la excepcion que
    * va a manejar. En este caso, quiero que maneje las excepciones de tipo InvalidLoginException.
    *
    * Esta funcion devuelve un objeto de tipo ResponseEntity (Es un tipo especial de objeto que nos facilita el trabajo, porque
    * nos permite encapsular nuestro mensaje de error o cualquier otra cosa, más el codigo de error HTTP para los clientes
    * que hicieron la solicitud al sistema).
    * */
    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLogin(InvalidLoginException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(InvalidLogoutException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLogout(InvalidLogoutException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    /* Solo por si acaso sucede una excepción no identificada en el servidor, quiero que notifique a los usuarios para
    que no se queden esperando una respuesta para siempre.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message("Unexpected error: " + ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
