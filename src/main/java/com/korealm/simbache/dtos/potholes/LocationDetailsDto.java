package com.korealm.simbache.dtos.potholes;

import com.korealm.simbache.dtos.geography.LocalityDto;
import com.korealm.simbache.dtos.geography.MunicipalityDto;
import com.korealm.simbache.dtos.geography.StateDto;
import com.korealm.simbache.dtos.geography.StreetDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDetailsDto {
    private long locationId;
    private int postalCode;
    private StateDto state;
    private MunicipalityDto municipality;
    private LocalityDto locality;
    private StreetDto mainStreet;
    private StreetDto streetOne; // nullable
    private StreetDto streetTwo; // nullable
}
