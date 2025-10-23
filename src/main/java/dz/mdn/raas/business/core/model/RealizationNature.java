/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RealizationNature
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
@Entity(name="RealizationNature")
@Table(name="T_02_01_04", uniqueConstraints = { @UniqueConstraint(name = "T_02_01_04_UK_01", columnNames = { "F_03" }),
											 	@UniqueConstraint(name = "T_02_01_04_UK_02", columnNames = { "F_03" })})
public class RealizationNature {
	
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

}
/*
INSERT INTO T_02_01_04 (F_00, F_01, F_02, F_03) VALUES
(1,'الوسائل الكبرى','Major Means','Moyens Majeurs'),
(2,'تسيير','Operation','Fonctionnement'),
(3,'تجهيز','Equipment','Équipement'),
(4,'قطع غيار','Spare Parts','Pièces de Rechange'),
(5,'أشغال','Constructions','Travaux'),
(6,'خدمات','Services','Prestations'),
(7,'دراسات','Studies','Études');
*/