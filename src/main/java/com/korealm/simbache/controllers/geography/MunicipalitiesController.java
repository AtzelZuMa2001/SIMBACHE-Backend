package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.services.geography.MunicipalitiesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/geography/municipalities")
@RequiredArgsConstructor
public class MunicipalitiesController {
    private final MunicipalitiesServiceImpl service;

    @GetMapping("/list")
    public ResponseEntity<Map<Short, String>> getByState(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam short stateId
    ) {
        var response = service.getMunicipalitiesByState(token, stateId);
        return ResponseEntity.ok(response);
    }
}
