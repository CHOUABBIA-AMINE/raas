/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Business / Consultation
 *
 **/

package dz.mdn.raas.business.consultation.repository;

import dz.mdn.raas.business.consultation.model.Consultation;
import dz.mdn.raas.business.consultation.model.AwardMethod;
import dz.mdn.raas.business.consultation.model.ConsultationStep;
import dz.mdn.raas.business.core.model.ApprovalStatus;
import dz.mdn.raas.business.core.model.RealizationStatus;
import dz.mdn.raas.business.core.model.RealizationNature;
import dz.mdn.raas.business.plan.model.BudgetType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Enhanced Consultation Repository with optimized queries
 * Based on exact field names from the repository model
 */
@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long>, JpaSpecificationExecutor<Consultation> {

    /**
     * Find consultation by reference (F_03)
     */
    @Query("SELECT c FROM Consultation c WHERE c.reference = :reference")
    Optional<Consultation> findByReference(@Param("reference") String reference);

    /**
     * Find consultation by internal ID and year (F_01 + F_02)
     */
    @Query("SELECT c FROM Consultation c WHERE c.internalId = :internalId AND c.consultationYear = :year")
    Optional<Consultation> findByInternalIdAndConsultationYear(@Param("internalId") String internalId, 
                                                              @Param("year") String consultationYear);

    /**
     * Find consultations by year (F_02)
     */
    @Query("SELECT c FROM Consultation c WHERE c.consultationYear = :year ORDER BY c.internalId ASC")
    Page<Consultation> findByConsultationYear(@Param("year") String consultationYear, Pageable pageable);

    /**
     * Find consultations by realization status (F_18)
     */
    @Query("SELECT c FROM Consultation c WHERE c.realizationStatus = :status ORDER BY c.startDate DESC")
    Page<Consultation> findByRealizationStatus(@Param("status") RealizationStatus realizationStatus, Pageable pageable);

    /**
     * Find consultations by approval status (F_19)
     */
    @Query("SELECT c FROM Consultation c WHERE c.approvalStatus = :status ORDER BY c.approvalDate DESC")
    Page<Consultation> findByApprovalStatus(@Param("status") ApprovalStatus approvalStatus, Pageable pageable);

    /**
     * Find consultations by award method (F_15)
     */
    @Query("SELECT c FROM Consultation c WHERE c.awardMethod = :awardMethod ORDER BY c.publishDate DESC")
    Page<Consultation> findByAwardMethod(@Param("awardMethod") AwardMethod awardMethod, Pageable pageable);

    /**
     * Find consultations by realization nature (F_16)
     */
    @Query("SELECT c FROM Consultation c WHERE c.realizationNature = :nature ORDER BY c.allocatedAmount DESC")
    Page<Consultation> findByRealizationNature(@Param("nature") RealizationNature realizationNature, Pageable pageable);

    /**
     * Find consultations by budget type (F_17)
     */
    @Query("SELECT c FROM Consultation c WHERE c.budgetType = :budgetType ORDER BY c.startDate DESC")
    Page<Consultation> findByBudgetType(@Param("budgetType") BudgetType budgetType, Pageable pageable);

    /**
     * Find consultations by consultation step (F_21)
     */
    @Query("SELECT c FROM Consultation c WHERE c.consultationStep = :step ORDER BY c.publishDate DESC")
    Page<Consultation> findByConsultationStep(@Param("step") ConsultationStep consultationStep, Pageable pageable);

    /**
     * Find consultations by date range (F_09 - startDate)
     */
    @Query("SELECT c FROM Consultation c WHERE c.startDate BETWEEN :startDate AND :endDate ORDER BY c.startDate ASC")
    Page<Consultation> findByStartDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    /**
     * Find consultations by deadline range (F_13)
     */
    @Query("SELECT c FROM Consultation c WHERE c.deadline BETWEEN :fromDate AND :toDate ORDER BY c.deadline ASC")
    Page<Consultation> findByDeadlineBetween(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

    /**
     * Find consultations by allocated amount range (F_07)
     */
    @Query("SELECT c FROM Consultation c WHERE c.allocatedAmount BETWEEN :minAmount AND :maxAmount ORDER BY c.allocatedAmount DESC")
    Page<Consultation> findByAllocatedAmountBetween(@Param("minAmount") double minAmount, @Param("maxAmount") double maxAmount, Pageable pageable);

    /**
     * Search consultations by designation (F_04, F_05, F_06)
     */
    @Query("SELECT c FROM Consultation c WHERE " +
           "LOWER(c.designationAr) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.designationEn) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.designationFr) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.reference) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.approvalReference) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY c.startDate DESC")
    Page<Consultation> searchByDesignation(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find consultation by ID with all relationships loaded
     */
    @Query("SELECT c FROM Consultation c " +
           "LEFT JOIN FETCH c.awardMethod " +
           "LEFT JOIN FETCH c.realizationNature " +
           "LEFT JOIN FETCH c.budgetType " +
           "LEFT JOIN FETCH c.realizationStatus " +
           "LEFT JOIN FETCH c.approvalStatus " +
           "LEFT JOIN FETCH c.realizationDirector " +
           "LEFT JOIN FETCH c.consultationStep " +
           "LEFT JOIN FETCH c.documents " +
           "LEFT JOIN FETCH c.referencedMails " +
           "LEFT JOIN FETCH c.plannedItems " +
           "WHERE c.id = :id")
    Optional<Consultation> findByIdWithDetails(@Param("id") Long id);

    /**
     * Find expired consultations (deadline passed)
     */
    @Query("SELECT c FROM Consultation c WHERE c.deadline < :currentDate AND c.realizationStatus.id NOT IN :completedStatusIds ORDER BY c.deadline ASC")
    Page<Consultation> findExpiredConsultations(@Param("currentDate") Date currentDate, @Param("completedStatusIds") List<Long> completedStatusIds, Pageable pageable);

    /**
     * Find consultations expiring soon
     */
    @Query("SELECT c FROM Consultation c WHERE c.deadline BETWEEN :currentDate AND :futureDate AND c.realizationStatus.id NOT IN :completedStatusIds ORDER BY c.deadline ASC")
    Page<Consultation> findConsultationsExpiringSoon(@Param("currentDate") Date currentDate, @Param("futureDate") Date futureDate, @Param("completedStatusIds") List<Long> completedStatusIds, Pageable pageable);

    /**
     * Find active consultations (published and not expired)
     */
    @Query("SELECT c FROM Consultation c WHERE c.publishDate <= :currentDate AND (c.deadline IS NULL OR c.deadline > :currentDate) AND c.realizationStatus.id NOT IN :completedStatusIds ORDER BY c.publishDate DESC")
    Page<Consultation> findActiveConsultations(@Param("currentDate") Date currentDate, @Param("completedStatusIds") List<Long> completedStatusIds, Pageable pageable);

    /**
     * Get max internal ID for a year (for generating next ID)
     */
    @Query("SELECT CAST(MAX(c.internalId) AS long) FROM Consultation c WHERE c.consultationYear = :year")
    Long findMaxInternalIdByYear(@Param("year") String consultationYear);

    /**
     * Count consultations by year
     */
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.consultationYear = :year")
    Long countByConsultationYear(@Param("year") String consultationYear);

    /**
     * Count active consultations by year
     */
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.consultationYear = :year AND c.realizationStatus.id NOT IN :completedStatusIds")
    Long countActiveConsultationsByYear(@Param("year") String consultationYear, @Param("completedStatusIds") List<Long> completedStatusIds);

    /**
     * Count completed consultations by year
     */
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.consultationYear = :year AND c.realizationStatus.id IN :completedStatusIds")
    Long countCompletedConsultationsByYear(@Param("year") String consultationYear, @Param("completedStatusIds") List<Long> completedStatusIds);

    /**
     * Sum allocated amount by year
     */
    @Query("SELECT COALESCE(SUM(c.allocatedAmount), 0.0) FROM Consultation c WHERE c.consultationYear = :year")
    Double sumAllocatedAmountByYear(@Param("year") String consultationYear);

    /**
     * Sum financial estimation by year
     */
    @Query("SELECT COALESCE(SUM(c.financialEstimation), 0.0) FROM Consultation c WHERE c.consultationYear = :year")
    Double sumFinancialEstimationByYear(@Param("year") String consultationYear);

    /**
     * Average submissions per consultation
     */
    @Query("SELECT AVG(SIZE(c.submissions)) FROM Consultation c WHERE c.consultationYear = :year")
    Double averageSubmissionsPerConsultation(@Param("year") String consultationYear);

    /**
     * Count consultations by status
     */
    @Query("SELECT c.realizationStatus.id, COUNT(c) FROM Consultation c GROUP BY c.realizationStatus.id")
    List<Object[]> countConsultationsByStatus();

    /**
     * Count consultations by award method
     */
    @Query("SELECT c.awardMethod.id, COUNT(c) FROM Consultation c GROUP BY c.awardMethod.id")
    List<Object[]> countConsultationsByAwardMethod();

    /**
     * Count consultations by budget type
     */
    @Query("SELECT c.budgetType.id, COUNT(c) FROM Consultation c GROUP BY c.budgetType.id")
    List<Object[]> countConsultationsByBudgetType();

    /**
     * Find consultations with most submissions
     */
    @Query("SELECT c FROM Consultation c ORDER BY SIZE(c.submissions) DESC")
    Page<Consultation> findConsultationsWithMostSubmissions(Pageable pageable);

    /**
     * Find consultations without submissions
     */
    @Query("SELECT c FROM Consultation c WHERE SIZE(c.submissions) = 0 ORDER BY c.publishDate DESC")
    Page<Consultation> findConsultationsWithoutSubmissions(Pageable pageable);

    /**
     * Update consultation step
     */
    @Modifying
    @Query("UPDATE Consultation c SET c.consultationStep = :consultationStep WHERE c.id = :id")
    int updateConsultationStep(@Param("id") Long id, @Param("consultationStep") ConsultationStep consultationStep);

    /**
     * Update realization status
     */
    @Modifying
    @Query("UPDATE Consultation c SET c.realizationStatus = :realizationStatus WHERE c.id = :id")
    int updateRealizationStatus(@Param("id") Long id, @Param("realizationStatus") RealizationStatus realizationStatus);

    /**
     * Batch update approval status for multiple consultations
     */
    @Modifying
    @Query("UPDATE Consultation c SET c.approvalStatus = :approvalStatus, c.approvalDate = :approvalDate WHERE c.id IN :ids")
    int batchUpdateApprovalStatus(@Param("ids") List<Long> consultationIds, @Param("approvalStatus") ApprovalStatus approvalStatus, @Param("approvalDate") Date approvalDate);

    /**
     * Find consultations by multiple criteria (optimized native query)
     */
    @Query(value = """
        SELECT c.* FROM T_02_04_04 c 
        LEFT JOIN T_02_04_01 am ON c.F_15 = am.F_00
        LEFT JOIN T_02_01_02 rn ON c.F_16 = rn.F_00  
        LEFT JOIN T_02_02_01 bt ON c.F_17 = bt.F_00
        LEFT JOIN T_02_01_03 rs ON c.F_18 = rs.F_00
        LEFT JOIN T_02_01_01 as_tbl ON c.F_19 = as_tbl.F_00
        WHERE (:year IS NULL OR c.F_02 = :year)
        AND (:awardMethodId IS NULL OR c.F_15 = :awardMethodId)
        AND (:realizationStatusId IS NULL OR c.F_18 = :realizationStatusId)
        AND (:approvalStatusId IS NULL OR c.F_19 = :approvalStatusId)
        AND (:minAmount IS NULL OR c.F_07 >= :minAmount)
        AND (:maxAmount IS NULL OR c.F_07 <= :maxAmount)
        AND (:startDate IS NULL OR c.F_09 >= :startDate)
        AND (:endDate IS NULL OR c.F_09 <= :endDate)
        ORDER BY c.F_09 DESC
        """, 
        countQuery = """
        SELECT COUNT(c.F_00) FROM T_02_04_04 c 
        WHERE (:year IS NULL OR c.F_02 = :year)
        AND (:awardMethodId IS NULL OR c.F_15 = :awardMethodId)
        AND (:realizationStatusId IS NULL OR c.F_18 = :realizationStatusId)
        AND (:approvalStatusId IS NULL OR c.F_19 = :approvalStatusId)
        AND (:minAmount IS NULL OR c.F_07 >= :minAmount)
        AND (:maxAmount IS NULL OR c.F_07 <= :maxAmount)
        AND (:startDate IS NULL OR c.F_09 >= :startDate)
        AND (:endDate IS NULL OR c.F_09 <= :endDate)
        """,
        nativeQuery = true)
    Page<Consultation> findConsultationsByCriteriaNative(
        @Param("year") String year,
        @Param("awardMethodId") Long awardMethodId,
        @Param("realizationStatusId") Long realizationStatusId,
        @Param("approvalStatusId") Long approvalStatusId,
        @Param("minAmount") Double minAmount,
        @Param("maxAmount") Double maxAmount,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        Pageable pageable
    );
}
