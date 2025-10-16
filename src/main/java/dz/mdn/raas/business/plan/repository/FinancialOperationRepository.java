/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FinancialOperationRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.raas.business.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.business.plan.model.FinancialOperation;

@Repository
public interface FinancialOperationRepository extends JpaRepository<FinancialOperation, Long> {

}