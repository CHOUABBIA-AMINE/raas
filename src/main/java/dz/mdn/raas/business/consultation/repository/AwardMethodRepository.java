/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AwardMethodRepository
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

import dz.mdn.raas.business.consultation.model.AwardMethod;

@Repository
public interface AwardMethodRepository extends JpaRepository<AwardMethod, Long> {

}