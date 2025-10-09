package dz.mdn.raas.bussiness.consultation.repository;

import dz.mdn.raas.bussiness.consultation.model.Consultation;
import dz.mdn.raas.bussiness.core.model.ApprovalStatus;
import dz.mdn.raas.bussiness.core.model.RealizationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Consultation entity operations
 * Manages consultation data access and complex queries
 */
@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    /**
     * Find consultation by reference number
     * @param reference the consultation reference to search for
     * @return optional consultation with matching reference
     */
    Optional<Consultation> findByReference(String reference);

    /**
     * Find consultations by title containing (case insensitive)
     * @param title the partial title to search for
     * @return list of consultations containing the title
     */
    @Query("SELECT c FROM Consultation c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Consultation> findByTitleContainingIgnoreCase(@Param("title") String title);

    /**
     * Find consultations by approval status
     * @param approvalStatus the approval status to filter by
     * @return list of consultations with matching approval status
     */
    List<Consultation> findByApprovalStatus(ApprovalStatus approvalStatus);

    /**
     * Find consultations by realization status
     * @param realizationStatus the realization status to filter by
     * @return list of consultations with matching realization status
     */
    List<Consultation> findByRealizationStatus(RealizationStatus realizationStatus);

    /**
     * Find consultations by estimated amount range
     * @param minAmount minimum estimated amount
     * @param maxAmount maximum estimated amount
     * @return list of consultations within the amount range
     */
    @Query("SELECT c FROM Consultation c WHERE c.estimatedAmount BETWEEN :minAmount AND :maxAmount")
    List<Consultation> findByEstimatedAmountBetween(@Param("minAmount") BigDecimal minAmount, 
                                                   @Param("maxAmount") BigDecimal maxAmount);

    /**
     * Find consultations by publication date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of consultations within the date range
     */
    @Query("SELECT c FROM Consultation c WHERE c.publicationDate BETWEEN :startDate AND :endDate")
    List<Consultation> findByPublicationDateBetween(@Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate);

    /**
     * Find consultations with deadline after specified date
     * @param date the date to compare against
     * @return list of consultations with deadline after the date
     */
    @Query("SELECT c FROM Consultation c WHERE c.deadline > :date")
    List<Consultation> findByDeadlineAfter(@Param("date") LocalDate date);

    /**
     * Find active consultations (deadline not passed)
     * @return list of active consultations
     */
    @Query("SELECT c FROM Consultation c WHERE c.deadline >= CURRENT_DATE")
    List<Consultation> findActiveConsultations();

    /**
     * Find expired consultations (deadline passed)
     * @return list of expired consultations
     */
    @Query("SELECT c FROM Consultation c WHERE c.deadline < CURRENT_DATE")
    List<Consultation> findExpiredConsultations();

    /**
     * Find consultations ordered by publication date desc
     * @param pageable pagination information
     * @return paginated list of consultations ordered by publication date
     */
    Page<Consultation> findAllByOrderByPublicationDateDesc(Pageable pageable);

    /**
     * Find consultations by estimated amount greater than
     * @param amount the minimum amount threshold
     * @return list of consultations with amount greater than threshold
     */
    List<Consultation> findByEstimatedAmountGreaterThan(BigDecimal amount);

    /**
     * Count consultations by approval status
     * @param approvalStatus the approval status to count
     * @return count of consultations with the status
     */
    long countByApprovalStatus(ApprovalStatus approvalStatus);

    /**
     * Check if consultation exists by reference
     * @param reference the consultation reference to check
     * @return true if exists, false otherwise
     */
    boolean existsByReference(String reference);

    /**
     * Find consultations published in current year
     * @return list of consultations published this year
     */
    @Query("SELECT c FROM Consultation c WHERE YEAR(c.publicationDate) = YEAR(CURRENT_DATE)")
    List<Consultation> findPublishedThisYear();
}