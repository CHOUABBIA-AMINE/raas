package dz.mdn.raas.common.communication.repository;

import dz.mdn.raas.common.communication.model.Mail;
import dz.mdn.raas.common.communication.model.MailNature;
import dz.mdn.raas.common.communication.model.MailType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Mail entity operations
 * Manages mail communication data access and queries
 */
@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {

    /**
     * Find mail by reference number
     * @param reference the mail reference to search for
     * @return optional mail with matching reference
     */
    Optional<Mail> findByReference(String reference);

    /**
     * Find mails by mail type
     * @param mailType the mail type to filter by
     * @return list of mails with matching type
     */
    List<Mail> findByMailType(MailType mailType);

    /**
     * Find mails by mail nature
     * @param mailNature the mail nature to filter by
     * @return list of mails with matching nature
     */
    List<Mail> findByMailNature(MailNature mailNature);

    /**
     * Find mails by subject containing (case insensitive)
     * @param subject the partial subject to search for
     * @return list of mails containing the subject
     */
    @Query("SELECT m FROM Mail m WHERE LOWER(m.subject) LIKE LOWER(CONCAT('%', :subject, '%'))")
    List<Mail> findBySubjectContainingIgnoreCase(@Param("subject") String subject);

    /**
     * Find mails by sender containing (case insensitive)
     * @param sender the partial sender name to search for
     * @return list of mails from senders containing the name
     */
    @Query("SELECT m FROM Mail m WHERE LOWER(m.sender) LIKE LOWER(CONCAT('%', :sender, '%'))")
    List<Mail> findBySenderContainingIgnoreCase(@Param("sender") String sender);

    /**
     * Find mails by recipient containing (case insensitive)
     * @param recipient the partial recipient name to search for
     * @return list of mails to recipients containing the name
     */
    @Query("SELECT m FROM Mail m WHERE LOWER(m.recipient) LIKE LOWER(CONCAT('%', :recipient, '%'))")
    List<Mail> findByRecipientContainingIgnoreCase(@Param("recipient") String recipient);

    /**
     * Find mails by reception date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of mails within the reception date range
     */
    @Query("SELECT m FROM Mail m WHERE m.receptionDate BETWEEN :startDate AND :endDate")
    List<Mail> findByReceptionDateBetween(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Find mails by send date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of mails within the send date range
     */
    @Query("SELECT m FROM Mail m WHERE m.sendDate BETWEEN :startDate AND :endDate")
    List<Mail> findBySendDateBetween(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);

    /**
     * Find mails received today
     * @return list of mails received today
     */
    @Query("SELECT m FROM Mail m WHERE DATE(m.receptionDate) = CURRENT_DATE")
    List<Mail> findReceivedToday();

    /**
     * Find mails sent today
     * @return list of mails sent today
     */
    @Query("SELECT m FROM Mail m WHERE DATE(m.sendDate) = CURRENT_DATE")
    List<Mail> findSentToday();

    /**
     * Find mails received after specified date
     * @param date the date to compare against
     * @return list of mails received after the date
     */
    List<Mail> findByReceptionDateAfter(LocalDateTime date);

    /**
     * Find mails sent after specified date
     * @param date the date to compare against
     * @return list of mails sent after the date
     */
    List<Mail> findBySendDateAfter(LocalDateTime date);

    /**
     * Find mails ordered by reception date desc
     * @param pageable pagination information
     * @return paginated list of mails ordered by reception date descending
     */
    Page<Mail> findAllByOrderByReceptionDateDesc(Pageable pageable);

    /**
     * Find mails by type and nature
     * @param mailType the mail type to filter by
     * @param mailNature the mail nature to filter by
     * @return list of mails matching both criteria
     */
    List<Mail> findByMailTypeAndMailNature(MailType mailType, MailNature mailNature);

    /**
     * Count mails by type
     * @param mailType the mail type to count
     * @return count of mails with the type
     */
    long countByMailType(MailType mailType);

    /**
     * Count mails by nature
     * @param mailNature the mail nature to count
     * @return count of mails with the nature
     */
    long countByMailNature(MailNature mailNature);

    /**
     * Check if mail exists by reference
     * @param reference the mail reference to check
     * @return true if exists, false otherwise
     */
    boolean existsByReference(String reference);

    /**
     * Find mails received in current month
     * @return list of mails received this month
     */
    @Query("SELECT m FROM Mail m WHERE YEAR(m.receptionDate) = YEAR(CURRENT_DATE) AND MONTH(m.receptionDate) = MONTH(CURRENT_DATE)")
    List<Mail> findReceivedThisMonth();

    /**
     * Find mails sent in current month
     * @return list of mails sent this month
     */
    @Query("SELECT m FROM Mail m WHERE YEAR(m.sendDate) = YEAR(CURRENT_DATE) AND MONTH(m.sendDate) = MONTH(CURRENT_DATE)")
    List<Mail> findSentThisMonth();

    /**
     * Find mails by sender ordered by reception date desc
     * @param sender the sender to filter by
     * @return list of mails ordered by reception date descending
     */
    @Query("SELECT m FROM Mail m WHERE LOWER(m.sender) = LOWER(:sender) ORDER BY m.receptionDate DESC")
    List<Mail> findBySenderOrderByReceptionDateDesc(@Param("sender") String sender);

    /**
     * Search mails by content (subject, sender, recipient)
     * @param searchTerm the search term to match
     * @return list of mails matching the search term
     */
    @Query("SELECT m FROM Mail m WHERE LOWER(m.subject) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.sender) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.recipient) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Mail> searchByContent(@Param("searchTerm") String searchTerm);
}