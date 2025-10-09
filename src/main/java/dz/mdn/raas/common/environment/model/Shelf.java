/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Shelf
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Environment
 *
 **/

package dz.mdn.raas.common.environment.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Shelf")
@Table(name="T_01_01_05", uniqueConstraints = { @UniqueConstraint(name = "T_01_01_05_UK_01", columnNames = { "F_01" })})
public class Shelf {
	
	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01", length=20, nullable=false)
	private String code;
	
	@ManyToOne
    @JoinColumn(name="F_02", foreignKey=@ForeignKey(name="T_01_01_05_FK_01"), nullable=false)
    private Room room;
	
	@OneToMany(mappedBy="shelf")
    private List<ArchiveBox> archiveBoxs;
	
}