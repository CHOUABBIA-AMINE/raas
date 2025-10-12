/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FolderRepository
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

import dz.mdn.raas.common.environment.model.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

}