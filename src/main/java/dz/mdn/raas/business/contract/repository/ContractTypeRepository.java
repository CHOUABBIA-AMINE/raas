/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ContractTypeRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Contract
 *
 **/

package dz.mdn.raas.business.contract.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.business.contract.model.ContractType;

@Repository
public interface ContractTypeRepository extends JpaRepository<ContractType, Long> {

}