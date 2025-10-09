package dz.mdn.raas.bussiness.plan.repository;

import dz.mdn.raas.bussiness.plan.model.BudgetModification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for BudgetModification entity operations
 * Manages budget modification data access and queries
 */
@Repository
public interface BudgetModificationRepository extends JpaRepository<BudgetModification, Long> {

    /**
     * Find budget modification by reference
     * @param reference the reference to search for
     * @return optional budget modification with matching reference
     */
    Optional<BudgetModification> findByReference(String reference);

    /**
     * Find budget modifications by amount range
     * @param minAmount minimum amount
     * @param maxAmount maximum amount
     * @return list of budget modifications within the amount range
     */
    @Query("SELECT bm FROM BudgetModification bm WHERE bm.amount BETWEEN :minAmount AND :maxAmount")
    List<BudgetModification> findByAmountBetween(@Param("minAmount") BigDecimal minAmount, 
                                               @Param("maxAmount") BigDecimal maxAmount);

    /**
     * Find budget modifications by modification date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of budget modifications within the date range
     */
    @Query("SELECT bm FROM BudgetModification bm WHERE bm.modificationDate BETWEEN :startDate AND :endDate")
    List<BudgetModification> findByModificationDateBetween(@Param("startDate") LocalDate startDate, 
                                                         @Param("endDate") LocalDate endDate);

    /**
     * Find budget modifications ordered by modification date desc
     * @return list of budget modifications ordered by modification date descending
     */
    List<BudgetModification> findAllByOrderByModificationDateDesc();

    /**
     * Find budget modifications by amount greater than
     * @param amount the minimum amount threshold
     * @return list of budget modifications with amount greater than threshold
     */
    List<BudgetModification> findByAmountGreaterThan(BigDecimal amount);

    /**
     * Check if budget modification exists by reference
     * @param reference the reference to check
     * @return true if exists, false otherwise
     */
    boolean existsByReference(String reference);

    /**
     * Find budget modifications created in current year
     * @return list of budget modifications created this year
     */
    @Query("SELECT bm FROM BudgetModification bm WHERE YEAR(bm.modificationDate) = YEAR(CURRENT_DATE)")
    List<BudgetModification> findCreatedThisYear();

    /**
     * Calculate total budget modifications amount
     * @return total amount of all budget modifications
     */
    @Query("SELECT SUM(bm.amount) FROM BudgetModification bm")
    BigDecimal calculateTotalModificationAmount();
}