package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.StateDto;

import java.util.List;

public interface StatesService {
    List<StateDto> getAllStates(String token);

    short addState(String token, String stateName);
    void updateState(String token, BasicUpdateDto basicUpdateDto);
    void deleteState(String token, short stateId);
}
