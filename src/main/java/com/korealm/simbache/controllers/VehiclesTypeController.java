package com.korealm.simbache.controllers;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.services.VehicleTypeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles/type")
@RequiredArgsConstructor
public class VehiclesTypeController {
    private final VehicleTypeServiceImpl service;

    @GetMapping("/list")
    public ResponseEntity<List<String>> getAll(@RequestHeader("X-Auth-Token") String token) {
        var response = service.getAllVehicleTypes(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Short> addVehicleType(@RequestHeader("X-Auth-Token") String token, @Valid @RequestHeader String newType) {
        var newVehicleTypeId = service.addVehicleType(token, newType);
        return ResponseEntity.ok(newVehicleTypeId);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateVehicleType(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody BasicUpdateDto dto) {
        service.updateVehicleType(token, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void>  deleteVehicleType(@RequestHeader("X-Auth-Token") String token, @RequestParam String typeName) {
        service.deleteVehicleType(token, typeName);
        return ResponseEntity.ok().build();

    }
}
