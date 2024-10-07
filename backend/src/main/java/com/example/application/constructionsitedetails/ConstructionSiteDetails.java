package com.example.application.constructionsitedetails;

import java.time.LocalDateTime;

import com.example.application.constructionsite.ConstructionSite;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
public class ConstructionSiteDetails {
    
    @Id
    @SequenceGenerator(
        name = "constructionsite_id_sequence",
        sequenceName = "constructionsite_id_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "constructionsite_id_sequence"
    )
    private long id;

    private long numberOfTransformers;
    private long numberOfExpansionTanks;
    private long numberOfRadiators;
    private long numberOfConnectionPoints;
    private long numberOfFirewalls;
    private LocalDateTime lastDayUploaded;

    @OneToOne(mappedBy = "details")
    @JsonIgnore
    private ConstructionSite constructionSite;
}
