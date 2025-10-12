/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationRepository
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

import dz.mdn.raas.bussiness.consultation.model.Consultation;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

}