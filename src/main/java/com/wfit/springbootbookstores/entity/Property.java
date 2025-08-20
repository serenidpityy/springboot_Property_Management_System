package com.wfit.springbootbookstores.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Property")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "house_number", nullable = false, unique = true, length = 50)
    private String houseNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "building", nullable = false, length = 50)
    private String building;

    @Column(name = "unit", nullable = false, length = 50)
    private String unit;

    @Column(name = "area", precision = 10, scale = 2)
    private BigDecimal area;

    @Column(name = "house_type", length = 50)
    private String houseType;

    @Column(name = "additional_description", columnDefinition = "TEXT")
    private String additionalDescription;

    @Column(name = "occupancy_info", length = 255)
    private String occupancyInfo;
}
