package com.korealm.simbache.services;

import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.exceptions.UserDoesntExistException;
import com.korealm.simbache.models.AuditLog;
import com.korealm.simbache.models.SessionToken;
import com.korealm.simbache.models.User;
import com.korealm.simbache.repositories.AuditLogRepository;
import com.korealm.simbache.repositories.UserRepository;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLoggingServiceImpl implements AuditLoggingService {
    private final AuditLogRepository auditLogRepository;
    private final VerificationService verificationService;
    private final UserRepository userRepository;

    public void log(User user, String action, String details) {
        if (user == null) return;
        var log = AuditLog.builder()
                .user(user)
                .action(action)
                .details(details)
                .build();
        auditLogRepository.save(log);
    }

    public void logByToken(SessionToken token, String action, String details) {
        if (token == null || token.getUser() == null) return;
        log(token.getUser(), action, details);
    }

    public void logException(HttpServletRequest request, Exception ex) {
        String tokenHeader = request.getHeader("X-Auth-Token");
        String path = request.getRequestURI();
        String action = "EXCEPCION";
        String details = "Se lanzó " + ex.getClass().getSimpleName() + ": '" + ex.getMessage() + "' en la ruta " + path + ".";

        var token = verificationService.validateProvidedToken(tokenHeader);
        if (token.isEmpty()) return;

        logByToken(token.get(), action, details);
    }

    public List<AuditLog> listAllLogs(String token) {
        if (!verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");
        return auditLogRepository.findAllByOrderByLogIdDesc();
    }

    public Page<AuditLog> listLogsPaginated(String token, int page, int size) {
        if (!verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findAllByOrderByLogIdDesc(pageable);
    }

    public List<AuditLog> listLogsForUser(String token, Long userId) {
        if (!verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        // Jalamos el usuario del que estamos intentando encontrar los registros
        var user = userRepository.findById(userId).orElseThrow(() -> new UserDoesntExistException("El usuario solicitado no existe."));

        return auditLogRepository.findAllByUserOrderByLogIdDesc(user);
    }

    public List<AuditLog> listLogsForUserRole(String token, short roleId) {
        if (!verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");
        return auditLogRepository.findAllByUser_UserRole_RoleIdOrderByLogIdDesc(roleId);
    }
}
