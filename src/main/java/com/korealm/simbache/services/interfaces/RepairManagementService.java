package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.repairs.PotholeRepairDto;

public interface RepairManagementService {
    PotholeRepairDto getRepairData(String token, Long potholeId);

    void saveRepair(String token, PotholeRepairDto dto);
}
