package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.geography.ZipCodeLookupDto;

import java.util.List;

public interface ZipCodeLookupService {

    /**
     * Returns the StateId, MunicipalityId, and LocalityId based on a zip code.
     * Since multiple localities can share the same postal code, this returns a list.
     */
    List<ZipCodeLookupDto> getLocationsByZipCode(String token, int postalCode);

    /**
     * Returns the Zip code based on stateId, municipalityId, and localityId.
     */
    ZipCodeLookupDto getZipCodeByLocation(String token, short stateId, short municipalityId, int localityId);
}