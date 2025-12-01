package com.korealm.simbache.services;

import com.korealm.simbache.dtos.repairs.PotholeRepairDto;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.*;
import com.korealm.simbache.repositories.*;
import com.korealm.simbache.services.interfaces.RepairManagementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepairManagementServiceImpl implements RepairManagementService {

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
    @Override
    public PotholeRepairDto getRepairData(String token, Long potholeId) {
        // 1. Verificación de sesión
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para continuar.");

        Pothole pothole = potholeRepository.findByPotholeId(potholeId)
                .orElseThrow(() -> new RuntimeException("El reporte de bache no existe."));

        Optional<Repair> repairOpt = repairRepository.findByPothole(pothole);

        if (repairOpt.isPresent() && !repairOpt.get().isActive()) {
            repairOpt = Optional.empty();
        }

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
    @Override
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

            // Si existía pero estaba borrada lógicamente, la reactivamos al guardar
            if (!repair.isActive()) {
                repair.setActive(true);
            }
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

    /**
     * Realiza un BORRADO LÓGICO (Soft Delete) desactivando la reparación.
     */
    @Transactional
    @Override
    public void deleteRepair(String token, Long potholeId) {
        // 1. Solo un Admin puede eliminar
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para eliminar reparaciones.");

        // 2. Buscamos el bache y su reparación
        Pothole pothole = potholeRepository.findByPotholeId(potholeId)
                .orElseThrow(() -> new RuntimeException("El reporte no existe."));

        Repair repair = repairRepository.findByPothole(pothole)
                .orElseThrow(() -> new RuntimeException("No existe reparación activa para este reporte."));

        // 3. APAGAMOS EL SWITCH (Borrado Lógico)
        repair.setActive(false);
        repairRepository.save(repair);

        // 4. Log de auditoría
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_REPARACION",
                        "El usuario " + u.getUsername() + " eliminó (lógicamente) la reparación del bache #" + potholeId)
        );
    }


    /**
     * Obtiene los catálogos mapeados a objetos simples para evitar referencias circulares.
     */
    @Override
    public Map<String, Object> getCatalogs(String token) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("Usuario no autorizado");

        // 1. Obtenemos las entidades de la BD
        List<Squad> squadsEntities = squadRepository.findAll();
        List<RepairStatus> statusEntities = repairStatusRepository.findAll();

        // 2. Mapeamos Squads a objetos simples (DTOs)
        // Esto evita que Jackson intente serializar el Contractor -> Location -> State -> Loop infinito
        List<Map<String, Object>> simpleSquads = squadsEntities.stream().map(sq -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("squadId", sq.getSquadId());
            dto.put("squadName", sq.getSquadName());
            return dto;
        }).toList();

        // 3. Mapeamos Statuses (Aunque suelen ser seguros, es mejor prevenir)
        List<Map<String, Object>> simpleStatuses = statusEntities.stream().map(st -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("statusId", st.getStatusId());
            dto.put("statusName", st.getStatusName());
            return dto;
        }).toList();

        // 4. Empaquetamos todo
        Map<String, Object> catalogs = new HashMap<>();
        catalogs.put("squads", simpleSquads);
        catalogs.put("statuses", simpleStatuses);

        return catalogs;
    }
}