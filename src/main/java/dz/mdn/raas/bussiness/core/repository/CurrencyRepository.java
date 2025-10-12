/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: CurrencyRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Pakage		: Business / Core
 *
 **/

package dz.mdn.raas.bussiness.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.bussiness.core.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

}