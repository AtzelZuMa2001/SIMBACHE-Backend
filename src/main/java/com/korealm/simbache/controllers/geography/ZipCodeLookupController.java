package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.dtos.geography.ZipCodeLookupDto;
import com.korealm.simbache.services.geography.ZipCodeLookupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geography/zipcode")
@RequiredArgsConstructor
public class ZipCodeLookupController {

    private final ZipCodeLookupServiceImpl service;

    /**
     * Returns the StateId, MunicipalityId, and LocalityId based on a zip code.
     *
     * @param token     Authentication token
     * @param postalCode The postal/zip code to search
     * @return List of locations matching the postal code
     */
    @GetMapping("/lookup")
    public ResponseEntity<List<ZipCodeLookupDto>> getLocationsByZipCode(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam int postalCode
    ) {
        var response = service.getLocationsByZipCode(token, postalCode);
        return ResponseEntity.ok(response);
    }

    /**
     * Returns the Zip code based on stateId, municipalityId, and localityId.
     *
     * @param token         Authentication token
     * @param stateId       The state ID
     * @param municipalityId The municipality ID
     * @param localityId    The locality ID
     * @return The location data including the postal code
     */
    @GetMapping("/reverse-lookup")
    public ResponseEntity<ZipCodeLookupDto> getZipCodeByLocation(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam short stateId,
            @RequestParam short municipalityId,
            @RequestParam int localityId
    ) {
        var response = service.getZipCodeByLocation(token, stateId, municipalityId, localityId);
        return ResponseEntity.ok(response);
    }
}