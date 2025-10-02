package com.korealm.simbache.services;

import com.korealm.simbache.dtos.login.LoginRequest;
import com.korealm.simbache.dtos.login.LoginResponse;
import com.korealm.simbache.exceptions.InvalidLoginException;
import com.korealm.simbache.models.SessionToken;
import com.korealm.simbache.models.User;
import com.korealm.simbache.repositories.SessionTokenRepository;
import com.korealm.simbache.repositories.UserRepository;
import com.korealm.simbache.services.interfaces.LoginService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Genera un constructor con todos los argumentos que tiene la clase como argumentos final para que no podamos cambiar los valores de los atributos.
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;
    private final SessionTokenRepository sessionTokenRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        /* Como dije en los DTO, Optional es un objeto en Java que puede o no contener un valor. Es la forma moderna, segura de trabajar en Java sin que nos tire un NullPointerException cuando no hay valor.

        El Optional recibe entre los <> el tipo de objeto que puede contener. En este caso el tipo es User.
         */
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());

        /* Los objetos Optional tienen los métodos .get() para obtener el objeto, o un .isEmpty() para verificar si tiene
            algo adentro.
         */
        if (optionalUser.isEmpty()) {
            throw new InvalidLoginException("Received data is invalid, it is not a valid user (username and password)");
        }

        User user = optionalUser.get();

        // Comparamos la contraseña hasheada que envió el frontend, con la que tenemos en la BD
        if (!user.getPasswordHash().equals(request.getPasswordHash())) {
            throw new InvalidLoginException("Invalid credentials");
        }

        /* En este punto ya verificamos que el usuario y la contraseña hacen match. Asi que podemos seguir adelante.

        Eliminamos cualquier token de autenticación que exista en la BD para este usuario (para obligar a que solo
            pueda tener 1 sesión activa a la vez.
         */
        sessionTokenRepository.findByUserFk(user.getUserId())
                .ifPresent(sessionTokenRepository::delete);

        /* Aquí podrían preguntarse que rayos es ese :: de arriba de este comentario
            En Java, para pasar una variable como parámetro, simplemente ponemos el nombre de la variable, no?
            Bueno, si en vez de una variable queremos pasar una función, ponemos NombreDeLaClase::NombreDeLaFuncion
            Y eso es como pasar una referencia a la función, como un puntero.
         */

        // Genera un nuevo token de sesión para este usuario.
        String newTokenId = UUID.randomUUID().toString();

        SessionToken sessionToken = SessionToken.builder()
                .tokenId(newTokenId)
                .userFk(user.getUserId())
                .build();

        sessionTokenRepository.save(sessionToken);

        // 5. Construir el objeto que vamos a enviar de respuesta de regreso al cliente que hizo la petición de login.
        return LoginResponse.builder()
                .token(newTokenId)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    /*  Para el logout no es necesario volver a verificar la contraseña, o las credenciales en general.
        Esto porque la petición que envía el usuario ya debe tener dentro de si el token de autenticación.

        E incluso si un atacante consigue el token y nos lo envía, esto simplemente cerraría la sesión del usuario nada más.
     */

    /* Todos los métodos de delete, o que causen un delete en la BD, deben ser declarados con @Transactional.
     *   Y tal y como dice, esto hace que la operación se realice en una transacción, por si llega a fallar algo,
     *    hace un auto rollback.
     */
    @Transactional
    @Override
    public void logout(String tokenId) {
        sessionTokenRepository.deleteByTokenId(tokenId);
    }
}
