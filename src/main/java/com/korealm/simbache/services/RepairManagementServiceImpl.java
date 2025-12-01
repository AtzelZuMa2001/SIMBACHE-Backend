package com.korealm.simbache.services;

import com.korealm.simbache.dtos.repairs.PotholeRepairDto;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.*;
import com.korealm.simbache.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepairManagementServiceImpl {

    // Repositorios
    private final PotholeRepository potholeRepository;
    private final RepairRepository repairRepository;
    private final ContractorRepository contractorRepository;
    private final RepairStatusRepository repairStatusRepository;
    private final RepairSquadRepository repairSquadRepository;
    private final SquadRepository squadRepository;

    // Servicios de seguridad y auditoría
    private final VerificationService verificationService;
    private final AuditLoggingServiceImpl auditLoggingService;

    /**
     * Obtiene los datos de reparación.
     * Requiere que el usuario esté autenticado (cualquier rol válido).
     */
    public PotholeRepairDto getRepairData(String token, Long potholeId) {
        // 1. Verificación de sesión
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para continuar.");

        Pothole pothole = potholeRepository.findByPotholeId(potholeId)
                .orElseThrow(() -> new RuntimeException("El reporte de bache no existe."));

        Optional<Repair> repairOpt = repairRepository.findByPothole(pothole);

        // 2. Construcción del DTO
        var dtoBuilder = PotholeRepairDto.builder()
                .potholeId(pothole.getPotholeId())
                .dateReported(pothole.getDateReported())
                .streetName(pothole.getLocation().getMainStreet().getStreetName())
                .citizenName(pothole.getReportByCitizen() != null ?
                        pothole.getReportByCitizen().getFirstName() + " " + pothole.getReportByCitizen().getLastName() : "Anónimo");

        String calle1 = pothole.getLocation().getStreetOne() != null ? pothole.getLocation().getStreetOne().getStreetName() : "";
        String calle2 = pothole.getLocation().getStreetTwo() != null ? pothole.getLocation().getStreetTwo().getStreetName() : "";
        dtoBuilder.betweenStreets(calle1 + (calle2.isEmpty() ? "" : " y " + calle2));

        if (repairOpt.isPresent()) {
            Repair repair = repairOpt.get();
            dtoBuilder.repairId(repair.getRepairId())
                    .startDate(repair.getStartDate())
                    .endDate(repair.getEndDate())
                    .statusId(repair.getStatus().getStatusId());

            // Buscar la cuadrilla asignada
            Optional<RepairSquad> repairSquadOpt = repairSquadRepository.findByRepair(repair);
            if (repairSquadOpt.isPresent()) {
                dtoBuilder.squadId(repairSquadOpt.get().getSquad().getSquadId());
            }
        }

        // 3. Log de auditoría (Consulta)
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_REPARACION", "El usuario " + u.getUsername() + " consultó la reparación del bache #" + potholeId)
        );

        return dtoBuilder.build();
    }

    /**
     * Guarda o actualiza la reparación.
     * Requiere permisos de ADMINISTRADOR.
     */
    @Transactional
    public void saveRepair(String token, PotholeRepairDto dto) {
        // 1. Verificación de permisos de Administrador
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para modificar reparaciones.");

        Pothole pothole = potholeRepository.findById(dto.getPotholeId())
                .orElseThrow(() -> new RuntimeException("Bache no existe"));

        Repair repair;
        boolean isNew = false;

        Optional<Repair> existing = repairRepository.findByPothole(pothole);

        if (existing.isPresent()) {
            repair = existing.get();
        } else {
            repair = new Repair();
            repair.setPothole(pothole);
            repair.setBudget(BigDecimal.ZERO); // Default
            Contractor defaultContractor = contractorRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Error interno: No hay contratistas registrados para asignar por defecto."));
            repair.setContractor(defaultContractor);
            isNew = true;
        }

        // 2. Actualización de campos
        repair.setStartDate(dto.getStartDate());
        repair.setEndDate(dto.getEndDate());

        RepairStatus status = repairStatusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Estado inválido"));
        repair.setStatus(status);

        // Guardar entidad principal Repair
        repair = repairRepository.save(repair);

        // 3. Manejo de Cuadrilla (RepairSquads)
        if (dto.getSquadId() != null) {
            Squad squad = squadRepository.findById(dto.getSquadId())
                    .orElseThrow(() -> new RuntimeException("Cuadrilla no encontrada"));

            RepairSquad repairSquad = repairSquadRepository.findByRepair(repair)
                    .orElse(new RepairSquad());

            repairSquad.setRepair(repair);
            repairSquad.setSquad(squad);

            if (repairSquad.getAssignedDate() == null) {
                repairSquad.setAssignedDate(dto.getStartDate());
            }
            // Si el estado es "Terminado" (asumiendo ID X), podríamos setear ReleasedDate aquí,
            // pero por ahora lo dejamos simple.

            repairSquadRepository.save(repairSquad);
        }

        // 4. Log de auditoría (Modificación)
        String actionType = isNew ? "CREACION_REPARACION" : "ACTUALIZACION_REPARACION";
        String detailMsg = isNew
                ? " creó una asignación de reparación para el bache #" + dto.getPotholeId()
                : " actualizó la reparación del bache #" + dto.getPotholeId();

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, actionType, "El usuario " + u.getUsername() + detailMsg)
        );
    }
}