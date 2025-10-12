/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EconomicNatureRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Provider
 *
 **/

package dz.mdn.raas.bussiness.provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.bussiness.provider.model.EconomicNature;

@Repository
public interface EconomicNatureRepository extends JpaRepository<EconomicNature, Long> {

}