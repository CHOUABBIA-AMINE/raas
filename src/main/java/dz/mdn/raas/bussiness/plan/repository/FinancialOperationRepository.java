package dz.mdn.raas.bussiness.plan.repository;

import dz.mdn.raas.bussiness.plan.model.FinancialOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for FinancialOperation entity operations
 * Manages financial operation data access and queries
 */
@Repository
public interface FinancialOperationRepository extends JpaRepository<FinancialOperation, Long> {

    /**
     * Find financial operation by name
     * @param name the operation name to search for
     * @return optional financial operation with matching name
     */
    Optional<FinancialOperation> findByName(String name);

    /**
     * Find financial operations by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of financial operations containing the name
     */
    @Query("SELECT fo FROM FinancialOperation fo WHERE LOWER(fo.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<FinancialOperation> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all financial operations ordered by name
     * @return list of financial operations ordered by name ascending
     */
    List<FinancialOperation> findAllByOrderByNameAsc();

    /**
     * Check if financial operation exists by name
     * @param name the operation name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find financial operation by name (case insensitive)
     * @param name the operation name to search for
     * @return optional financial operation with matching name
     */
    @Query("SELECT fo FROM FinancialOperation fo WHERE LOWER(fo.name) = LOWER(:name)")
    Optional<FinancialOperation> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active financial operations
     * @return list of active financial operations
     */
    @Query("SELECT fo FROM FinancialOperation fo WHERE fo.active = true")
    List<FinancialOperation> findAllActive();

    /**
     * Count active financial operations
     * @return number of active financial operations
     */
    @Query("SELECT COUNT(fo) FROM FinancialOperation fo WHERE fo.active = true")
    long countActive();
}