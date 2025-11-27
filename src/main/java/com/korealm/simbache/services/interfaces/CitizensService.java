package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.citizens.CitizenCreateDto;
import com.korealm.simbache.dtos.citizens.CitizenLookupDto;
import com.korealm.simbache.dtos.citizens.CitizenResponseDto;
import com.korealm.simbache.dtos.citizens.CitizenUpdateDto;

public interface CitizensService {

    /**
     * Lookup a citizen by phone number.
     */
    CitizenLookupDto lookupByPhoneNumber(String token, Long phoneNumber);

    /**
     * Get citizen full details by ID.
     */
    CitizenResponseDto getById(String token, Long citizenId);

    /**
     * Create a new citizen.
     */
    Long add(String token, CitizenCreateDto dto);

    /**
     * Update an existing citizen.
     */
    void update(String token, Long citizenId, CitizenUpdateDto dto);
}