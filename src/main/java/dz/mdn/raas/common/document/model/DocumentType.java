/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DocumentType
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Document
 *
 **/

package dz.mdn.raas.common.document.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity(name="DocumentType")
@Table(name="T_01_03_01", uniqueConstraints = { @UniqueConstraint(name = "T_01_03_01_UK_01", columnNames = { "F_03", "F_04" })})
public class DocumentType {
	
	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01", length=200)
	private String designationAr;

	@Column(name="F_02", length=200)
	private String designationEn;
	
	@Column(name="F_03", length=200)
	private String designationFr;
	
	@Column(name="F_04")
	private int scope;
	
	@OneToMany(mappedBy="documentType")
    private List<Document> documents;

}