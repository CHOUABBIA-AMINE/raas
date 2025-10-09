package dz.mdn.raas.bussiness.plan.repository;

import dz.mdn.raas.bussiness.plan.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for PlannedItem entity operations
 * Manages planned item data access and complex queries
 */
@Repository
public interface PlannedItemRepository extends JpaRepository<PlannedItem, Long> {

    /**
     * Find planned items by item
     * @param item the item to filter by
     * @return list of planned items for the item
     */
    List<PlannedItem> findByItem(Item item);

    /**
     * Find planned items by domain
     * @param domain the domain to filter by
     * @return list of planned items for the domain
     */
    List<PlannedItem> findByDomain(Domain domain);

    /**
     * Find planned items by rubric
     * @param rubric the rubric to filter by
     * @return list of planned items for the rubric
     */
    List<PlannedItem> findByRubric(Rubric rubric);

    /**
     * Find planned items by item status
     * @param itemStatus the item status to filter by
     * @return list of planned items with matching status
     */
    List<PlannedItem> findByItemStatus(ItemStatus itemStatus);

    /**
     * Find planned items by financial operation
     * @param financialOperation the financial operation to filter by
     * @return list of planned items for the financial operation
     */
    List<PlannedItem> findByFinancialOperation(FinancialOperation financialOperation);

    /**
     * Find planned items by budget type
     * @param budgetType the budget type to filter by
     * @return list of planned items for the budget type
     */
    List<PlannedItem> findByBudgetType(BudgetType budgetType);

    /**
     * Find planned items by item distribution
     * @param itemDistribution the item distribution to filter by
     * @return list of planned items for the item distribution
     */
    List<PlannedItem> findByItemDistribution(ItemDistribution itemDistribution);

    /**
     * Find planned items by estimated amount range
     * @param minAmount minimum estimated amount
     * @param maxAmount maximum estimated amount
     * @return list of planned items within the amount range
     */
    @Query("SELECT pi FROM PlannedItem pi WHERE pi.estimatedAmount BETWEEN :minAmount AND :maxAmount")
    List<PlannedItem> findByEstimatedAmountBetween(@Param("minAmount") BigDecimal minAmount, 
                                                  @Param("maxAmount") BigDecimal maxAmount);

    /**
     * Find planned items by planning date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of planned items within the date range
     */
    @Query("SELECT pi FROM PlannedItem pi WHERE pi.planningDate BETWEEN :startDate AND :endDate")
    List<PlannedItem> findByPlanningDateBetween(@Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);

    /**
     * Find planned items by domain and budget type
     * @param domain the domain to filter by
     * @param budgetType the budget type to filter by
     * @return list of planned items matching both criteria
     */
    List<PlannedItem> findByDomainAndBudgetType(Domain domain, BudgetType budgetType);

    /**
     * Find planned items ordered by planning date desc
     * @param pageable pagination information
     * @return paginated list of planned items ordered by planning date
     */
    Page<PlannedItem> findAllByOrderByPlanningDateDesc(Pageable pageable);

    /**
     * Find planned items by estimated amount greater than
     * @param amount the minimum amount threshold
     * @return list of planned items with amount greater than threshold
     */
    List<PlannedItem> findByEstimatedAmountGreaterThan(BigDecimal amount);

    /**
     * Count planned items by domain
     * @param domain the domain to count items for
     * @return count of planned items for the domain
     */
    long countByDomain(Domain domain);

    /**
     * Count planned items by item status
     * @param itemStatus the item status to count
     * @return count of planned items with the status
     */
    long countByItemStatus(ItemStatus itemStatus);

    /**
     * Find planned items created in current year
     * @return list of planned items created this year
     */
    @Query("SELECT pi FROM PlannedItem pi WHERE YEAR(pi.planningDate) = YEAR(CURRENT_DATE)")
    List<PlannedItem> findPlannedThisYear();

    /**
     * Calculate total estimated amount by domain
     * @param domain the domain to calculate total for
     * @return total estimated amount for the domain
     */
    @Query("SELECT SUM(pi.estimatedAmount) FROM PlannedItem pi WHERE pi.domain = :domain")
    BigDecimal calculateTotalEstimatedAmountByDomain(@Param("domain") Domain domain);

    /**
     * Calculate total estimated amount by budget type
     * @param budgetType the budget type to calculate total for
     * @return total estimated amount for the budget type
     */
    @Query("SELECT SUM(pi.estimatedAmount) FROM PlannedItem pi WHERE pi.budgetType = :budgetType")
    BigDecimal calculateTotalEstimatedAmountByBudgetType(@Param("budgetType") BudgetType budgetType);

    /**
     * Find planned items by domain ordered by estimated amount desc
     * @param domain the domain to filter by
     * @return list of planned items ordered by estimated amount descending
     */
    List<PlannedItem> findByDomainOrderByEstimatedAmountDesc(Domain domain);

    /**
     * Find top planned items by estimated amount
     * @param limit maximum number of items to return
     * @return list of top planned items by amount
     */
    @Query(value = "SELECT pi FROM PlannedItem pi ORDER BY pi.estimatedAmount DESC")
    List<PlannedItem> findTopByEstimatedAmount(Pageable pageable);

    /**
     * Find planned items by multiple criteria
     * @param domain the domain to filter by (optional)
     * @param budgetType the budget type to filter by (optional)
     * @param itemStatus the item status to filter by (optional)
     * @return list of planned items matching the criteria
     */
    @Query("SELECT pi FROM PlannedItem pi WHERE " +
           "(:domain IS NULL OR pi.domain = :domain) AND " +
           "(:budgetType IS NULL OR pi.budgetType = :budgetType) AND " +
           "(:itemStatus IS NULL OR pi.itemStatus = :itemStatus)")
    List<PlannedItem> findByCriteria(@Param("domain") Domain domain,
                                   @Param("budgetType") BudgetType budgetType,
                                   @Param("itemStatus") ItemStatus itemStatus);
}