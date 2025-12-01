package com.korealm.simbache.dtos.repairs;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PotholeRepairDto {
    // Datos de Lectura (Vienen del Pothole)
    private Long potholeId;
    private String citizenName;     // Nombre completo concatenado
    private LocalDateTime dateReported;
    private String streetName;
    private String betweenStreets;

    // Datos de Escritura (Van a Repair y RepairSquads)
    private Long repairId;          // Null si es nuevo
    private Integer squadId;        // ID de la cuadrilla seleccionada
    private LocalDate startDate;
    private LocalDate endDate;
    private Short statusId;
}
