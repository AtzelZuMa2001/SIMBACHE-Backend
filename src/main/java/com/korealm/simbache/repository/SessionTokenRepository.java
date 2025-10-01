package com.korealm.simbache.repository;

import com.korealm.simbache.model.SessionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SessionTokenRepository extends JpaRepository<SessionToken, String> {

    // Busca el token en la base de datos para validar la sesión del usuario
    Optional<SessionToken> findByTokenId(String tokenId);

    // Encontrar el token asociado al usuario, o encontrar token por usuario, pues.
    Optional<SessionToken> findByUserFk(Long userFk);

    // Borrar un token (logout)
    void deleteByTokenId(String tokenId);

    void deleteByUserFk(Long userFk);
}
