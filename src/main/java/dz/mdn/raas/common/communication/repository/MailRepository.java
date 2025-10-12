/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MailRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Communication
 *
 **/

package dz.mdn.raas.common.communication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.mdn.raas.common.communication.model.Mail;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {

}