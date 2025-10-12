/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ShelfRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Environment
 *
 **/

package dz.mdn.raas.common.environment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.common.environment.model.Shelf;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {

}