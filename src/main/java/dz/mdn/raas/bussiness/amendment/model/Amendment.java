/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Amendment
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Amendment
 *
 **/

package dz.mdn.raas.bussiness.amendment.model;

import java.util.Date;
import java.util.List;

import dz.mdn.raas.bussiness.contract.model.Contract;
import dz.mdn.raas.bussiness.core.model.ApprovalStatus;
import dz.mdn.raas.bussiness.core.model.Currency;
import dz.mdn.raas.bussiness.core.model.RealizationStatus;
import dz.mdn.raas.common.communication.model.Mail;
import dz.mdn.raas.common.document.model.Document;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Entity(name="Amendment")
@Table(name="T_02_06_04", uniqueConstraints = { @UniqueConstraint(name = "T_02_06_04_UK_01", columnNames = { "F_02" })})
public class Amendment {
	
	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01")
	private int internalId;
	
	@Column(name="F_02")
	private String reference;
	
	@Column(name="F_03", length=300)
	private String designationAr;

	@Column(name="F_04", length=300)
	private String designationEn;
	
	@Column(name="F_05", length=300)
	private String designationFr;
	
	@Column(name="F_06")
	private double amount;
	
	@Column(name="F_07")
	private double transferableAmount;
	
	@Column(name="F_08")
	private Date startDate;
	
	@Column(name="F_09")
	private Date approvalDate;
	
	@Column(name="F_10")
	private Date notifyDate;

	@Column(name="F_11", length=500)
	private String observation;
	
	@ManyToOne
    @JoinColumn(name="F_12", foreignKey=@ForeignKey(name="T_02_06_04_FK_01"), nullable=false)
    private Contract contract;
	
	@ManyToOne
    @JoinColumn(name="F_13", foreignKey=@ForeignKey(name="T_02_06_04_FK_02"), nullable=false)
    private AmendmentType amendmentType;

	@ManyToOne
    @JoinColumn(name="F_14", foreignKey=@ForeignKey(name="T_02_06_04_FK_03"), nullable=false)
    private RealizationStatus realizationStatus;
	
	@ManyToOne
    @JoinColumn(name="F_15", foreignKey=@ForeignKey(name="T_02_06_04_FK_04"), nullable=false)
    private AmendmentPhase amendmentStep;
	
	@ManyToOne
    @JoinColumn(name="F_16", foreignKey=@ForeignKey(name="T_02_06_04_FK_05"), nullable=true)
    private ApprovalStatus approvalStatus;
	
	@ManyToOne
    @JoinColumn(name="F_17", foreignKey=@ForeignKey(name="T_02_06_04_FK_06"), nullable=false)
    private Currency currency;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020604_T010302", 
			joinColumns = @JoinColumn(name = "F_01", foreignKey=@ForeignKey(name="R_T020604_T010302_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", foreignKey=@ForeignKey(name="R_T020604_T010302_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020604_T010302_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Document> documents;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "R_T020604_T010203", 
			joinColumns = @JoinColumn(name = "F_01", foreignKey = @ForeignKey(name = "R_T020604_T010203_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", foreignKey = @ForeignKey(name = "R_T020604_T010203_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T020604_T010203_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Mail> referencedMails;

}