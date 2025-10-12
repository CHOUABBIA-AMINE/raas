/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: DocumentRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Document
 *
 **/

package dz.mdn.raas.common.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.common.document.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}