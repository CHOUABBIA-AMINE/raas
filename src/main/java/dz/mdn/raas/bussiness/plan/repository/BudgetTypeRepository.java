package dz.mdn.raas.bussiness.plan.repository;

import dz.mdn.raas.bussiness.plan.model.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for BudgetType entity operations
 * Manages budget type data access and queries
 */
@Repository
public interface BudgetTypeRepository extends JpaRepository<BudgetType, Long> {

    /**
     * Find budget type by name
     * @param name the type name to search for
     * @return optional budget type with matching name
     */
    Optional<BudgetType> findByName(String name);

    /**
     * Find budget types by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of budget types containing the name
     */
    @Query("SELECT bt FROM BudgetType bt WHERE LOWER(bt.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<BudgetType> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all budget types ordered by name
     * @return list of budget types ordered by name ascending
     */
    List<BudgetType> findAllByOrderByNameAsc();

    /**
     * Check if budget type exists by name
     * @param name the type name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find budget type by name (case insensitive)
     * @param name the type name to search for
     * @return optional budget type with matching name
     */
    @Query("SELECT bt FROM BudgetType bt WHERE LOWER(bt.name) = LOWER(:name)")
    Optional<BudgetType> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active budget types
     * @return list of active budget types
     */
    @Query("SELECT bt FROM BudgetType bt WHERE bt.active = true")
    List<BudgetType> findAllActive();

    /**
     * Count active budget types
     * @return number of active budget types
     */
    @Query("SELECT COUNT(bt) FROM BudgetType bt WHERE bt.active = true")
    long countActive();
}