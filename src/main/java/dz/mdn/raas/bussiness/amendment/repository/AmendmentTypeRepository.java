/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentTypeRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Amendment
 *
 **/

package dz.mdn.raas.bussiness.amendment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.bussiness.amendment.model.AmendmentType;

@Repository
public interface AmendmentTypeRepository extends JpaRepository<AmendmentType, Long> {

}