package com.korealm.simbache.services.geography;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.StateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.State;
import com.korealm.simbache.repositories.StateRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.StatesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatesServiceImpl implements StatesService {
    private final StateRepository stateRepository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public List<StateDto> getAllStates(String token) {
        if (verificationService.isUserUnauthorized(token)) throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var result = stateRepository.findAll();

        // Log de auditoría: consulta de estados
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_ESTADOS", "El usuario " + u.getUsername() + " consultó la lista de estados (" + result.size() + ") elementos).")
        );

        return result
                .stream()
                .map(StateDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public short addState(String token, String stateName) {
        if (!verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        if (stateRepository.findByStateName(stateName).isPresent())
            throw new InvalidInsertException("El estado ya existe. No es posible crearlo de nuevo.");

        var state = State.builder()
                .stateName(stateName)
                .build();

        var saved = stateRepository.save(state);

        // Log de auditoría: creación
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CREACION_ESTADO", "El usuario " + u.getUsername() + " creó el estado '" + stateName + "' con id " + saved.getStateId() + ".")
        );

        return saved.getStateId();
    }

    @Override
    public void updateState(String token, BasicUpdateDto basicUpdateDto) {
        if (!verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var state = stateRepository
                .findByStateName(basicUpdateDto.getCurrentName())
                .orElseThrow(() -> new InvalidUpdateException("El estado no existe. No es posible actualizarlo."));

        state.setStateName(basicUpdateDto.getNewName());
        stateRepository.save(state);

        // Log de auditoría: actualización
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ACTUALIZACION_ESTADO", "El usuario " + u.getUsername() + " actualizó el estado de '" + basicUpdateDto.getCurrentName() + "' a '" + basicUpdateDto.getNewName() + "'.")
        );
    }

    @Override
    @Transactional
    public void deleteState(String token, short stateId) {
        if (!verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var state = stateRepository.findByStateId(stateId)
                .orElseThrow(() -> new InvalidUpdateException("El estado no existe. No es posible borrarlo."));

        stateRepository.deleteById(stateId);

        // Log de auditoría: eliminación
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_ESTADO", "El usuario " + u.getUsername() + " eliminó el estado '" + state.getStateName() + "' (id " + stateId + ").")
        );
    }

}
