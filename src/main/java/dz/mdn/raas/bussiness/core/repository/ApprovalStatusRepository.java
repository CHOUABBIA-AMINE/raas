package dz.mdn.raas.bussiness.core.repository;

import dz.mdn.raas.bussiness.core.model.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ApprovalStatus entity operations
 * Manages approval status data access and queries
 */
@Repository
public interface ApprovalStatusRepository extends JpaRepository<ApprovalStatus, Long> {

    /**
     * Find approval status by name
     * @param name the status name to search for
     * @return optional approval status with matching name
     */
    Optional<ApprovalStatus> findByName(String name);

    /**
     * Find approval statuses by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of approval statuses containing the name
     */
    @Query("SELECT a FROM ApprovalStatus a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ApprovalStatus> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all approval statuses ordered by name
     * @return list of approval statuses ordered by name ascending
     */
    List<ApprovalStatus> findAllByOrderByNameAsc();

    /**
     * Check if approval status exists by name
     * @param name the status name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find approval status by name (case insensitive)
     * @param name the status name to search for
     * @return optional approval status with matching name
     */
    @Query("SELECT a FROM ApprovalStatus a WHERE LOWER(a.name) = LOWER(:name)")
    Optional<ApprovalStatus> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Count total number of approval statuses
     * @return count of approval statuses
     */
    @Query("SELECT COUNT(a) FROM ApprovalStatus a")
    long countAll();
}