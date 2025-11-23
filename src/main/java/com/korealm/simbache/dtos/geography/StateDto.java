package com.korealm.simbache.dtos.geography;

import com.korealm.simbache.models.State;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class StateDto {
    public StateDto(State state) {
        this.stateId = state.getStateId();
        this.stateName = state.getStateName();
    }

    @NotBlank
    private short stateId;

    @NotBlank
    private String stateName;
}
