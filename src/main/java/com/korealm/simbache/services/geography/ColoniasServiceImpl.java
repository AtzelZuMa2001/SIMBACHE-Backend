package com.korealm.simbache.services.geography;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.ColoniaCreateDto;
import com.korealm.simbache.dtos.geography.ColoniaDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Colonia;
import com.korealm.simbache.repositories.ColoniaRepository;
import com.korealm.simbache.repositories.LocalityRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.ColoniasService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColoniasServiceImpl implements ColoniasService {
    private final ColoniaRepository coloniaRepository;
    private final LocalityRepository localityRepository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public List<ColoniaDto> getColoniasByLocality(String token, int localityId) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var colonias = coloniaRepository.findAllByLocality_LocalityId(localityId);

        // Audit log: colonias by locality
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_COLONIAS", "El usuario " + u.getUsername() +
                        " consultó la lista de colonias para la localidad " + localityId +
                        " (" + colonias.size() + ") elementos).")
        );

        return colonias
                .stream()
                .map(ColoniaDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public long addColonia(String token, ColoniaCreateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var locality = localityRepository.findByLocalityId(dto.getLocalityId())
                .orElseThrow(() -> new InvalidInsertException("La localidad especificada no existe. No es posible crear la colonia."));

        if (coloniaRepository.findByColoniaNameAndLocality(dto.getColoniaName(), locality).isPresent()) {
            throw new InvalidInsertException("La colonia con ese ID ya existe. No es posible crearla de nuevo.");
        }

        var colonia = Colonia.builder()
                .coloniaName(dto.getColoniaName())
                .locality(locality)
                .build();

        var saved = coloniaRepository.save(colonia);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CREACION_COLONIA", "El usuario " + u.getUsername() +
                        " creó la colonia '" + dto.getColoniaName() + "' con id " + saved.getColoniaId() +
                        " para la localidad " + locality.getLocalityId() + ".")
        );

        return saved.getColoniaId();
    }

    @Override
    public void updateColonia(String token, BasicUpdateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var colonia = coloniaRepository.findByColoniaName(dto.getCurrentName())
                .orElseThrow(() -> new InvalidUpdateException("La colonia no existe. No es posible actualizarla."));

        colonia.setColoniaName(dto.getNewName());
        coloniaRepository.save(colonia);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ACTUALIZACION_COLONIA", "El usuario " + u.getUsername() +
                        " actualizó la colonia de '" + dto.getCurrentName() + "' a '" + dto.getNewName() + "'.")
        );
    }

    @Override
    @Transactional
    public void deleteColonia(String token, long coloniaId) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var colonia = coloniaRepository.findByColoniaId(coloniaId)
                .orElseThrow(() -> new InvalidUpdateException("La colonia no existe. No es posible borrarla."));

        coloniaRepository.deleteById(coloniaId);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_COLONIA", "El usuario " + u.getUsername() +
                        " eliminó la colonia '" + colonia.getColoniaName() + "' (id " + coloniaId + ").")
        );
    }
}
