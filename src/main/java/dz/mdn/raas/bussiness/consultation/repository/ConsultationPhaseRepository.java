/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationPhaseRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Consultation
 *
 **/

package dz.mdn.raas.bussiness.consultation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.bussiness.consultation.model.ConsultationPhase;

@Repository
public interface ConsultationPhaseRepository extends JpaRepository<ConsultationPhase, Long> {

}