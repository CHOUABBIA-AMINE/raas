/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: CountryRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.common.administration.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

}