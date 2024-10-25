package com.example.application.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;

import com.example.application.constructionsite.ConstructionSite;
import com.example.application.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AppUser implements UserDetails, Principal{

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
    
    @Column(unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    
    @OneToMany(mappedBy="owner")
    private List<ConstructionSite> constructionSites;

    public List<ConstructionSite> getAllConstructionSites() {
        return this.constructionSites;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }
}