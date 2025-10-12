/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PersonRepository
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

import dz.mdn.raas.common.administration.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}