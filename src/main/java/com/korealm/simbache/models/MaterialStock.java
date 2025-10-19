package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MaterialStock")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StockId")
    private Long stockId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Material_FK", nullable = false)
    private Material material;

    @Column(name = "Quantity", nullable = false)
    private Long quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WarehouseLocation_FK", nullable = false)
    private Location warehouseLocation;
}