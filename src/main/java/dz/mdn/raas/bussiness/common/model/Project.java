/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Project
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Goal		: Common
 *
 **/

package dz.mdn.raas.bussiness.common.model;

import java.util.Date;

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
@Entity(name="Project")
@Table(name="T_20_05", uniqueConstraints = { @UniqueConstraint(name = "T_20_05_UK_01", columnNames = { "F_01", "F_02" })})
public class Project {
	
	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01", length=3, nullable=false)
	private String internalId;
	
	@Column(name="F_02", length=4, nullable=false)
	private String projectYear;
	
	@Column(name="F_03", length=300)
	private String designationAr;

	@Column(name="F_04", length=300)
	private String designationEn;
	
	@Column(name="F_05", length=300, nullable=false)
	private String designationFr;

	@Column(name="F_06", length=500)
	private String observation;
	
	@Column(name="F_07")
	private Date startDate;

}