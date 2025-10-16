/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryRank
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name="MilitaryRank")
@Table(name="T_01_04_05", uniqueConstraints = { @UniqueConstraint(name = "T_01_04_05_UK_01", columnNames = { "F_03" })})
public class MilitaryRank {
	
	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01", length=50)
	private String designationAr;
	
	@Column(name="F_02", length=50)
	private String designationEn;
	
	@Column(name="F_03", length=50, nullable=false)
	private String designationFr;
	
	@Column(name="F_04", length=10)
	private String abbreviationAr;
	
	@Column(name="F_05", length=10)
	private String abbreviationEn;
	
	@Column(name="F_06", length=10, nullable=false)
	private String abbreviationFr;
	
	@ManyToOne
    @JoinColumn(name="F_07", foreignKey=@ForeignKey(name="T_01_04_05_FK_01"), nullable=false)
    private MilitaryCategory militaryCategory;
	
}