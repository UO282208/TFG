package com.example.application.user;

import java.util.List;
import java.util.ArrayList;

import com.example.application.constructionsite.ConstructionSite;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Entity
@Table
public class AppUser{

    @Id
    @SequenceGenerator(
        name = "appuser_id_sequence",
        sequenceName = "appuser_id_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "appuser_id_sequence"
    )
    private Long id;

    private String name;
    private String password;
    private String email;

    @OneToMany(mappedBy="owner")
    private List<ConstructionSite> constructionSites;

    public AppUser (){
        constructionSites = new ArrayList<ConstructionSite>();
    }

    public AppUser (Long id, String name, String password, String email){
        setId(id);
        setName(name);
        setPassword(password);
        setEmail(email);
        constructionSites = new ArrayList<ConstructionSite>();
    }

    public AppUser (String name, String password, String email){
        setName(name);
        setPassword(password);
        setEmail(email);
        constructionSites = new ArrayList<ConstructionSite>();
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void addConstructionSite(ConstructionSite cs) {
        this.constructionSites.add(cs);
        if (cs.getOwner() != this) {
            cs.setOwner(this);
        }
    }

    public boolean removeConstructionSite(ConstructionSite cs) {
        cs.setOwner(null);
        return this.constructionSites.remove(cs);
    }

    public ConstructionSite getConstructionSite(ConstructionSite cs) {
        return this.constructionSites.get(this.constructionSites.indexOf(cs));
    }
}