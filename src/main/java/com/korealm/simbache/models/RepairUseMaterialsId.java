package com.korealm.simbache.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RepairUseMaterialsId implements Serializable {

    @Column(name = "MaterialId", nullable = false)
    private Long materialId;

    @Column(name = "RepairId", nullable = false)
    private Long repairId;
}
