/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryCategoryRepository
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

import dz.mdn.raas.common.administration.model.MilitaryCategory;

@Repository
public interface MilitaryCategoryRepository extends JpaRepository<MilitaryCategory, Long> {

}