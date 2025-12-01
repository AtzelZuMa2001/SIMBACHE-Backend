package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.repairs.PotholeRepairDto;

import java.util.Map;

public interface RepairManagementService {
    PotholeRepairDto getRepairData(String token, Long potholeId);

    void saveRepair(String token, PotholeRepairDto dto);

    void deleteRepair(String token, Long potholeId);

    Map<String, Object> getCatalogs(String token);
}
