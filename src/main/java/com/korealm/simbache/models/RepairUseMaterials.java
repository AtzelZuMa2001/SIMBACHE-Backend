package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RepairUseMaterials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairUseMaterials {

    @EmbeddedId
    private RepairUseMaterialsId id;

    @ManyToOne(optional = false)
    @MapsId("materialId")
    @JoinColumn(name = "MaterialId", nullable = false)
    private Material material;

    @ManyToOne(optional = false)
    @MapsId("repairId")
    @JoinColumn(name = "RepairId", nullable = false)
    private Repair repair;

    @Column(name = "QuantityUsed", nullable = false)
    private int quantityUsed;
}
