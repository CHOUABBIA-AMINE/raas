/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RealizationDirector
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Pakage		: Business / Core
 *
 **/

package dz.mdn.raas.business.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity(name="RealizationDirector")
@Table(name="T_02_01_03", uniqueConstraints = { @UniqueConstraint(name = "T_02_01_03_UK_01", columnNames = { "F_03" })})
public class RealizationDirector {
	
	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01", length=300)
	private String designationAr;

	@Column(name="F_02", length=300)
	private String designationEn;
	
	@Column(name="F_03", length=300, nullable=false)
	private String designationFr;

}