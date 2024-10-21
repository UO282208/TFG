package com.example.application.restriction;

import java.time.LocalDateTime;

import com.example.application.constructionsitedetails.ConstructionSiteDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restriction {

    @Id
    @SequenceGenerator(
        name = "restriction_id_sequence",
        sequenceName = "restriction_id_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "restriction_id_sequence"
    )
    private long id;

    private long transformers;
    private long expansionTanks;
    private long radiators;
    private long connectionPoints;
    private long firewalls;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean shouldAppear;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SITE_ID")
    @JsonIgnore
    private ConstructionSiteDetails site;
}
