/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ClearanceRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Provider
 *
 **/

package dz.mdn.raas.business.provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.business.provider.model.Clearance;

@Repository
public interface ClearanceRepository extends JpaRepository<Clearance, Long> {

}