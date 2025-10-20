package com.korealm.simbache.controllers;

import com.korealm.simbache.models.AuditLog;
import com.korealm.simbache.services.AuditLoggingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLoggingServiceImpl auditLogService;

    @GetMapping("/list")
    public ResponseEntity<List<AuditLog>> listAll(@RequestHeader("X-Auth-Token") String token) {
        return ResponseEntity.ok(auditLogService.listAllLogs(token));
    }

    @GetMapping("/list/paged")
    public ResponseEntity<Page<AuditLog>> listPaged(@RequestHeader("X-Auth-Token") String token,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(auditLogService.listLogsPaginated(token, page, size));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<AuditLog>> byUser(@RequestHeader("X-Auth-Token") String token, @PathVariable Long userId) {
        return ResponseEntity.ok(auditLogService.listLogsForUser(token, userId));
    }

    @GetMapping("/by-role/{roleId}")
    public ResponseEntity<List<AuditLog>> byRole(@RequestHeader("X-Auth-Token") String token, @PathVariable short roleId) {
        return ResponseEntity.ok(auditLogService.listLogsForUserRole(token, roleId));
    }
}
