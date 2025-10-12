/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ItemStatusRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.raas.bussiness.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.bussiness.plan.model.ItemStatus;

@Repository
public interface ItemStatusRepository extends JpaRepository<ItemStatus, Long> {

}