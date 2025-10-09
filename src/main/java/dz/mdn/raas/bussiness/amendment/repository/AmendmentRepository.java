package dz.mdn.raas.bussiness.amendment.repository;

import dz.mdn.raas.bussiness.amendment.model.Amendment;
import dz.mdn.raas.bussiness.amendment.model.AmendmentType;
import dz.mdn.raas.bussiness.contract.model.Contract;
import dz.mdn.raas.bussiness.core.model.ApprovalStatus;
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
 * Repository interface for Amendment entity operations
 * Manages amendment data access and complex queries
 */
@Repository
public interface AmendmentRepository extends JpaRepository<Amendment, Long> {

    /**
     * Find amendment by reference number
     * @param reference the amendment reference to search for
     * @return optional amendment with matching reference
     */
    Optional<Amendment> findByReference(String reference);

    /**
     * Find amendments by contract
     * @param contract the contract to filter by
     * @return list of amendments for the contract
     */
    List<Amendment> findByContract(Contract contract);

    /**
     * Find amendments by amendment type
     * @param amendmentType the amendment type to filter by
     * @return list of amendments with matching type
     */
    List<Amendment> findByAmendmentType(AmendmentType amendmentType);

    /**
     * Find amendments by approval status
     * @param approvalStatus the approval status to filter by
     * @return list of amendments with matching approval status
     */
    List<Amendment> findByApprovalStatus(ApprovalStatus approvalStatus);

    /**
     * Find amendments by title containing (case insensitive)
     * @param title the partial title to search for
     * @return list of amendments containing the title
     */
    @Query("SELECT a FROM Amendment a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Amendment> findByTitleContainingIgnoreCase(@Param("title") String title);

    /**
     * Find amendments by amount range
     * @param minAmount minimum amendment amount
     * @param maxAmount maximum amendment amount
     * @return list of amendments within the amount range
     */
    @Query("SELECT a FROM Amendment a WHERE a.amount BETWEEN :minAmount AND :maxAmount")
    List<Amendment> findByAmountBetween(@Param("minAmount") BigDecimal minAmount, 
                                       @Param("maxAmount") BigDecimal maxAmount);

    /**
     * Find amendments by creation date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of amendments within the date range
     */
    @Query("SELECT a FROM Amendment a WHERE a.creationDate BETWEEN :startDate AND :endDate")
    List<Amendment> findByCreationDateBetween(@Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);

    /**
     * Find amendments by contract ordered by creation date desc
     * @param contract the contract to filter by
     * @return list of amendments ordered by creation date descending
     */
    List<Amendment> findByContractOrderByCreationDateDesc(Contract contract);

    /**
     * Find amendments created after specified date
     * @param date the date to compare against
     * @return list of amendments created after the date
     */
    List<Amendment> findByCreationDateAfter(LocalDate date);

    /**
     * Find amendments ordered by creation date desc
     * @param pageable pagination information
     * @return paginated list of amendments ordered by creation date
     */
    Page<Amendment> findAllByOrderByCreationDateDesc(Pageable pageable);

    /**
     * Find amendments by amount greater than
     * @param amount the minimum amount threshold
     * @return list of amendments with amount greater than threshold
     */
    List<Amendment> findByAmountGreaterThan(BigDecimal amount);

    /**
     * Count amendments by contract
     * @param contract the contract to count amendments for
     * @return count of amendments for the contract
     */
    long countByContract(Contract contract);

    /**
     * Count amendments by approval status
     * @param approvalStatus the approval status to count
     * @return count of amendments with the status
     */
    long countByApprovalStatus(ApprovalStatus approvalStatus);

    /**
     * Check if amendment exists by reference
     * @param reference the amendment reference to check
     * @return true if exists, false otherwise
     */
    boolean existsByReference(String reference);

    /**
     * Find amendments created in current year
     * @return list of amendments created this year
     */
    @Query("SELECT a FROM Amendment a WHERE YEAR(a.creationDate) = YEAR(CURRENT_DATE)")
    List<Amendment> findCreatedThisYear();

    /**
     * Calculate total amendment amount for contract
     * @param contract the contract to calculate total amendments for
     * @return total amount of all amendments for the contract
     */
    @Query("SELECT SUM(a.amount) FROM Amendment a WHERE a.contract = :contract")
    BigDecimal calculateTotalAmendmentAmountByContract(@Param("contract") Contract contract);

    /**
     * Find latest amendment for contract
     * @param contract the contract to find latest amendment for
     * @return optional latest amendment for the contract
     */
    @Query("SELECT a FROM Amendment a WHERE a.contract = :contract ORDER BY a.creationDate DESC")
    Optional<Amendment> findLatestAmendmentByContract(@Param("contract") Contract contract);
}