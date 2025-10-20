package com.korealm.simbache.exceptions;

import com.korealm.simbache.dtos.login.ErrorResponseDto;
import com.korealm.simbache.services.AuditLoggingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final AuditLoggingServiceImpl auditLoggingService;

    public ErrorResponseDto buildErrorResponse(String message) {
        return ErrorResponseDto.builder()
                .message(message)
                .build();
    }

    /*
    * Los métodos deben declararse con la anotación ExceptionHandler, y recibe como parámetro la clase de la excepción que
    * va a manejar. En este caso, quiero que maneje las excepciones de tipo InvalidLoginException.
    *
    * Esta función devuelve un objeto de tipo ResponseEntity (Es un tipo especial de objeto que nos facilita el trabajo, porque
    * nos permite encapsular nuestro mensaje de error o cualquier otra cosa, más el código de error HTTP para los clientes
    * que hicieron la solicitud al sistema).
    * */
    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidLogin(InvalidLoginException ex, HttpServletRequest request) {
        auditLoggingService.logException(request, ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidLogoutException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidLogout(InvalidLogoutException ex, HttpServletRequest request) {
        auditLoggingService.logException(request, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(ex.getMessage()));
    }

    // Excepción para cuando el usuario no está autorizado o manda un token inválido
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidInsert(UnauthorizedAccessException ex, HttpServletRequest request) {
        auditLoggingService.logException(request, ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrorResponse(ex.getMessage()));
    }

    // Excepción para cuando el usuario quiere insertar un valor que ya existe en la base de datos.
    @ExceptionHandler(InvalidInsertException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidInsert(InvalidInsertException ex, HttpServletRequest request) {
        auditLoggingService.logException(request, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidUpdateException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidUpdate(InvalidUpdateException ex, HttpServletRequest request) {
        auditLoggingService.logException(request, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserDoesntExistException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNonExistence(UserDoesntExistException ex, HttpServletRequest request) {
        auditLoggingService.logException(request, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(ex.getMessage()));
    }


    /* Solo por si acaso sucede una excepción no identificada en el servidor, quiero que notifique a los usuarios para
    que no se queden esperando una respuesta para siempre.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        auditLoggingService.logException(request, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse("Unexpected error: " + ex.getMessage()));
    }
}
