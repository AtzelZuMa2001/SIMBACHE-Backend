package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.models.AuditLog;
import com.korealm.simbache.models.SessionToken;
import com.korealm.simbache.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuditLoggingService {
    void log(User user, String action, String details);
    void logByToken(SessionToken token, String action, String details);
    void logException(HttpServletRequest request, Exception ex);

    List<AuditLog> listAllLogs(String token);
    Page<AuditLog> listLogsPaginated(String token, int page, int size);
    List<AuditLog> listLogsForUser(String token, Long userId);
    List<AuditLog> listLogsForUserRole(String token, short roleId);
}
