/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProviderRepository
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

import dz.mdn.raas.business.provider.model.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

}