package com.korealm.simbache.exceptions;

/* Esta clase es simplemente un tipo de excepción personalizada.
    En lugar de tirar cualquier excepción generica que no da información real de lo que paso, o que incluso nos
    puede confundir entre si fue un error de la lógica del negocio, o de la JVM, pues creamos esta excepción.

    Es simple: Extendemos la clase padre Exception, y le pasamos nuestro mensaje personalizado de error.
 */

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super(message);
    }
}