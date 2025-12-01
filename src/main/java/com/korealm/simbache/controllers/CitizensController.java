package com.korealm.simbache.controllers;

import com.korealm.simbache.dtos.citizens.CitizenCreateDto;
import com.korealm.simbache.dtos.citizens.CitizenLookupDto;
import com.korealm.simbache.dtos.citizens.CitizenResponseDto;
import com.korealm.simbache.dtos.citizens.CitizenUpdateDto;
import com.korealm.simbache.services.CitizensServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citizens")
@RequiredArgsConstructor
public class CitizensController {

    private final CitizensServiceImpl service;

    /**
     * Lookup a citizen by phone number.
     * Used for auto-filling citizen information in forms.
     */
    @GetMapping("/lookup")
    public ResponseEntity<CitizenLookupDto> lookupByPhoneNumber(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam Long phoneNumber
    ) {
        var response = service.lookupByPhoneNumber(token, phoneNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * Get full citizen details by ID.
     */
    @GetMapping("/{citizenId}")
    public ResponseEntity<CitizenResponseDto> getById(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Long citizenId
    ) {
        var response = service.getById(token, citizenId);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new citizen.
     */
    @PostMapping("/add")
    public ResponseEntity<Long> add(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody CitizenCreateDto dto
    ) {
        var id = service.add(token, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    /**
     * Update an existing citizen.
     */
    @PutMapping("/update/{citizenId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Long citizenId,
            @Valid @RequestBody CitizenUpdateDto dto
    ) {
        service.update(token, citizenId, dto);
        return ResponseEntity.ok().build();
    }
}