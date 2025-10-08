/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Empowerment
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Goal		: Provider
 *
 **/

package dz.mdn.raas.bussiness.provider.model;

import java.util.Date;

import dz.mdn.raas.common.communication.model.Mail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity(name="Empowerment")
@Table(name="T_21_06")
public class Empowerment {
	
	@Id
	@Column(name="F_00")
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="F_01")
	private Date startDate;
	
	@Column(name="F_02")
	private Date endDate;
	
	@ManyToOne
    @JoinColumn(name="F_03", foreignKey=@ForeignKey(name="T_21_06_FK_01"), nullable=false)
    private Provider provider;
	
	@ManyToOne
    @JoinColumn(name="F_04", foreignKey=@ForeignKey(name="T_21_06_FK_02"), nullable=false)
    private ProviderRepresentator providerRepresentator;

	@ManyToOne
    @JoinColumn(name="F_05", foreignKey=@ForeignKey(name="T_21_06_FK_03"), nullable=true)
    private Mail reference;

}