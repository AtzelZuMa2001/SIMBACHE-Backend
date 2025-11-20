package com.korealm.simbache.controllers.geography;


import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.services.geography.StatesServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/geography/states")
@RequiredArgsConstructor
public class StatesController {
    private final StatesServiceImpl service;

    @GetMapping("/list")
    public ResponseEntity<Map<Short, String>> getStates(@RequestHeader("X-Auth-Token") String token) {
        var response = service.getAllStates(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Short> addState(@RequestHeader("X-Auth-Token") String token, @Valid @RequestHeader String stateName) {
        var newStateId = service.addState(token, stateName);
        return ResponseEntity.ok(newStateId);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateState(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody BasicUpdateDto dto) {
        service.updateState(token, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteState(@RequestHeader("X-Auth-Token") String token, @RequestParam short stateId) {
        service.deleteState(token, stateId);
        return ResponseEntity.ok().build();
    }
}
