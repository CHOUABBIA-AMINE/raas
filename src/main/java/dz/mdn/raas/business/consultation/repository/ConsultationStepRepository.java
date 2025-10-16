/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationStepRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Consultation
 *
 **/

package dz.mdn.raas.business.consultation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.business.consultation.model.ConsultationStep;

@Repository
public interface ConsultationStepRepository extends JpaRepository<ConsultationStep, Long> {

}