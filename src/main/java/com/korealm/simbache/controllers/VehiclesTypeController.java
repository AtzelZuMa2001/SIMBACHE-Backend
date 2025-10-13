package com.korealm.simbache.controllers;

import com.korealm.simbache.dtos.vehicles.VehicleTypeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles/type")
@RequiredArgsConstructor
public class VehiclesTypeController {
    @GetMapping("/list")
    public void getAll() {}

    @PostMapping("/add")
    public void addVehicleType(VehicleTypeDto dto) {}

    @PutMapping("/update")
    public void updateVehicleType(VehicleTypeDto dto) {}

    @DeleteMapping("/delete")
    public void deleteVehicleType(short typeId) {}
}
