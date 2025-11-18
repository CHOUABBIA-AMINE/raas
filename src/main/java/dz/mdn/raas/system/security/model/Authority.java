package dz.mdn.raas.system.security.model;

import java.util.HashSet;
import java.util.Set;

import dz.mdn.raas.configuration.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "authorities")
@Getter
@Setter
public class Authority extends BaseEntity {

	@NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 200)
    private String description;

    @OneToMany(mappedBy = "authority", fetch = FetchType.LAZY)
    private Set<Permission> permissions = new HashSet<>();
    
}