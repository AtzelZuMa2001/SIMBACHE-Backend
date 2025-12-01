package com.korealm.simbache.controllers;

import com.korealm.simbache.dtos.repairs.PotholeRepairDto;
import com.korealm.simbache.services.RepairManagementServiceImpl;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repair-management")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RepairManagementController {

    private final RepairManagementServiceImpl repairService;

    // Se asume que el token viene en el header "Authorization" o similar.
    // Ajusta el nombre del header ("token", "Authorization", etc.) según como lo maneje tu frontend.

    @GetMapping("/{potholeId}")
    public ResponseEntity<?> getRepairByReport(
            @RequestHeader(value = "X-Auth-Token", required = false) String token,
            @PathVariable Long potholeId) {
        try {
            // Si tu front manda "Bearer XXXXX", asegúrate de limpiar el string si tu VerificationService espera solo el token
            // token = token.replace("Bearer ", "");

            return ResponseEntity.ok(repairService.getRepairData(token, potholeId));
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveRepair(
            @RequestHeader(value = "X-Auth-Token", required = false) String token,
            @RequestBody PotholeRepairDto dto) {
        try {
            repairService.saveRepair(token, dto);
            return ResponseEntity.ok("Reparación guardada correctamente");
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{potholeId}")
    public ResponseEntity<?> deleteRepair(
            @RequestHeader(value = "X-Auth-Token", required = false) String token,
            @PathVariable Long potholeId) {
        try {
            repairService.deleteRepair(token, potholeId);
            return ResponseEntity.ok("Reparación eliminada correctamente.");
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}