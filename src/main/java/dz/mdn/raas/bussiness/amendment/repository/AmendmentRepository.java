/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentRepository
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

import dz.mdn.raas.bussiness.amendment.model.Amendment;

@Repository
public interface AmendmentRepository extends JpaRepository<Amendment, Long> {

}