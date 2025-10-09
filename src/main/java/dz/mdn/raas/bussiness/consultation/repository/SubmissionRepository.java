package dz.mdn.raas.bussiness.consultation.repository;

import dz.mdn.raas.bussiness.consultation.model.Consultation;
import dz.mdn.raas.bussiness.consultation.model.Submission;
import dz.mdn.raas.bussiness.provider.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Submission entity operations
 * Manages submission data access and queries
 */
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    /**
     * Find submissions by consultation
     * @param consultation the consultation to filter by
     * @return list of submissions for the consultation
     */
    List<Submission> findByConsultation(Consultation consultation);

    /**
     * Find submissions by provider
     * @param provider the provider to filter by
     * @return list of submissions from the provider
     */
    List<Submission> findByProvider(Provider provider);

    /**
     * Find submissions by consultation and provider
     * @param consultation the consultation to filter by
     * @param provider the provider to filter by
     * @return optional submission matching both consultation and provider
     */
    Optional<Submission> findByConsultationAndProvider(Consultation consultation, Provider provider);

    /**
     * Find submissions by consultation ordered by submission date
     * @param consultation the consultation to filter by
     * @return list of submissions ordered by submission date
     */
    List<Submission> findByConsultationOrderBySubmissionDateAsc(Consultation consultation);

    /**
     * Find submissions by consultation ordered by proposed amount
     * @param consultation the consultation to filter by
     * @return list of submissions ordered by proposed amount ascending
     */
    List<Submission> findByConsultationOrderByProposedAmountAsc(Consultation consultation);

    /**
     * Find submissions by proposed amount range
     * @param minAmount minimum proposed amount
     * @param maxAmount maximum proposed amount
     * @return list of submissions within the amount range
     */
    @Query("SELECT s FROM Submission s WHERE s.proposedAmount BETWEEN :minAmount AND :maxAmount")
    List<Submission> findByProposedAmountBetween(@Param("minAmount") BigDecimal minAmount, 
                                               @Param("maxAmount") BigDecimal maxAmount);

    /**
     * Find submissions by submission date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of submissions within the date range
     */
    @Query("SELECT s FROM Submission s WHERE s.submissionDate BETWEEN :startDate AND :endDate")
    List<Submission> findBySubmissionDateBetween(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);

    /**
     * Find submissions with lowest proposed amount for consultation
     * @param consultation the consultation to filter by
     * @return submission with the lowest proposed amount
     */
    @Query("SELECT s FROM Submission s WHERE s.consultation = :consultation AND s.proposedAmount = " +
           "(SELECT MIN(s2.proposedAmount) FROM Submission s2 WHERE s2.consultation = :consultation)")
    List<Submission> findLowestProposedAmountByConsultation(@Param("consultation") Consultation consultation);

    /**
     * Count submissions by consultation
     * @param consultation the consultation to count submissions for
     * @return number of submissions for the consultation
     */
    long countByConsultation(Consultation consultation);

    /**
     * Count submissions by provider
     * @param provider the provider to count submissions for
     * @return number of submissions from the provider
     */
    long countByProvider(Provider provider);

    /**
     * Find submissions by consultation with pagination
     * @param consultation the consultation to filter by
     * @param pageable pagination information
     * @return paginated list of submissions
     */
    Page<Submission> findByConsultation(Consultation consultation, Pageable pageable);

    /**
     * Find submissions submitted after specified date
     * @param date the date to compare against
     * @return list of submissions submitted after the date
     */
    List<Submission> findBySubmissionDateAfter(LocalDateTime date);

    /**
     * Find submissions by provider ordered by submission date desc
     * @param provider the provider to filter by
     * @return list of submissions ordered by submission date descending
     */
    List<Submission> findByProviderOrderBySubmissionDateDesc(Provider provider);

    /**
     * Check if submission exists for consultation and provider
     * @param consultation the consultation to check
     * @param provider the provider to check
     * @return true if submission exists, false otherwise
     */
    boolean existsByConsultationAndProvider(Consultation consultation, Provider provider);

    /**
     * Delete submissions by consultation
     * @param consultation the consultation to delete submissions for
     */
    void deleteByConsultation(Consultation consultation);
}