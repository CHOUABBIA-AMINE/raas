/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StructureRepository
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

import dz.mdn.raas.common.administration.model.Structure;

@Repository
public interface StructureRepository extends JpaRepository<Structure, Long> {

}