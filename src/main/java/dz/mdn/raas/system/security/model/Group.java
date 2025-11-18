package dz.mdn.raas.system.security.model;

import java.util.HashSet;
import java.util.Set;

import dz.mdn.raas.configuration.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "groups")
@Getter
@Setter
public class Group extends BaseEntity {

	@NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 200)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "group_roles",
            joinColumns = @JoinColumn(name = "group_fk"),
            inverseJoinColumns = @JoinColumn(name = "role_fk"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
    
}