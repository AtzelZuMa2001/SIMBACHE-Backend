package com.korealm.simbache.services;

import com.korealm.simbache.dtos.login.LoginRequestDto;
import com.korealm.simbache.dtos.login.LoginResponseDto;
import com.korealm.simbache.exceptions.InvalidLoginException;
import com.korealm.simbache.exceptions.InvalidLogoutException;
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
    public LoginResponseDto login(LoginRequestDto request) {
        // Hace la query para obtener el usuario, y si falla o no revuelve nada, lanza una excepción y regresa la LoginController.
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidLoginException("Invalid username or password"));

        // Comparamos la contraseña hasheada que envió el frontend, con la que tenemos en la BD
        if (!user.getPasswordHash().equals(request.getPasswordHash())) {
            throw new InvalidLoginException("Invalid credentials");
        }

        /* En este punto ya verificamos que el usuario y la contraseña hacen match. Asi que podemos seguir adelante.

        Si el usuario ya tenía tokens viejos, lo actualizamos por el nuevo con un update. Si no tenía tokens,
         pues hacemos un insert
         */
        SessionToken newToken = generateNewToken(user);
        sessionTokenRepository.findByUser(user)
                        .ifPresentOrElse(
                                oldToken -> oldToken.setTokenId(newToken.getTokenId()), // Update
                                () -> sessionTokenRepository.save(newToken) // Insert
                        );

        // 5. Construir el objeto que vamos a enviar de respuesta de regreso al cliente que hizo la petición de login.
        return LoginResponseDto.builder()
                .token(newToken.getTokenId())
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
        var token = sessionTokenRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new InvalidLogoutException("No se puede cerrar la sesión: el token no existe"));

        sessionTokenRepository.delete(token);
    }

    private SessionToken generateNewToken(User user) {
        String newTokenId = UUID.randomUUID().toString();

        return SessionToken.builder()
                .tokenId(newTokenId)
                .user(user)
                .build();
    }
}
