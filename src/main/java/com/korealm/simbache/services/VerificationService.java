package com.korealm.simbache.services;

import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.SessionToken;
import com.korealm.simbache.repositories.SessionTokenRepository;
import com.korealm.simbache.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final SessionTokenRepository sessionTokenRepository;
    private final UserRepository userRepository;

    // Devuelve el token de la sesión si el usuario está autorizado, envuelto en un Optional.
    public Optional<SessionToken> validateProvidedToken(String token) {
        var session = sessionTokenRepository.findByTokenId(token)
                .orElseThrow(() -> new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes."));

        return Optional.of(session);
    }

    public boolean isUserAuthorized(String token) {
        return validateProvidedToken(token).isPresent();
    }

    // Esta función solo se usa para comprobar si el usuario es administrador. Regresa un booleano, simple.
    public boolean isUserAdmin(String token) {
        var sessionToken = validateProvidedToken(token);

        if (sessionToken.isEmpty()) return false;

        var user = userRepository.findBySessionToken(sessionToken.get());

        // Administradores: por ahora consideramos roles 3 y 4 como admin (ajustar si cambian las reglas)
        return user.map(u -> u.getUserRole() != null && (u.getUserRole().getRoleId() == 3 || u.getUserRole().getRoleId() == 4)).orElse(false);
    }
}
