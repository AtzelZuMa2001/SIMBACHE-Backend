package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.BasicUpdateDto;

import java.util.Map;

public interface StatesService {
    Map<Short, String> getAllStates(String token);

    short addState(String token, String stateName);
    void updateState(String token, BasicUpdateDto basicUpdateDto);
    void deleteState(String token, short stateId);
}
