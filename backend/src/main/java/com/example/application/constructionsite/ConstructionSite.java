package com.example.application.constructionsite;

import com.example.application.user.AppUser;
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
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConstructionSite {

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

    private String name;
    private int numOfWorkers;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OWNER_ID")
    @ToString.Exclude
    private AppUser owner;

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public int getNumOfWorkers()
    {
        return this.numOfWorkers;
    }

    @JsonIgnore
    public AppUser getOwner()
    {
        return this.owner;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNumOfWorkers(int numOfWorkers)
    {
        this.numOfWorkers = numOfWorkers;
    }

    public void setOwner(AppUser owner)
    {
        this.owner = owner;
    }
}
