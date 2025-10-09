/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentStep
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Amendment
 *
 **/

package dz.mdn.raas.bussiness.amendment.model;

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
@Entity(name="AmendmentStep")
@Table(name="T_02_06_03", uniqueConstraints = { @UniqueConstraint(name = "T_02_06_03_UK_01", columnNames = { "F_03" })})
public class AmendmentStep {
	
	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01", length=200)
	private String designationAr;

	@Column(name="F_02", length=200)
	private String designationEn;
	
	@Column(name="F_03", length=200, nullable=false)
	private String designationFr;
	
	@ManyToOne
    @JoinColumn(name="F_04", foreignKey=@ForeignKey(name="T_02_06_03_FK_01"), nullable=false)
    private AmendmentPhase amendmentPhase;

}