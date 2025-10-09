package dz.mdn.raas.bussiness.core.repository;

import dz.mdn.raas.bussiness.core.model.RealizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RealizationStatus entity operations
 * Manages realization status data access and queries
 */
@Repository
public interface RealizationStatusRepository extends JpaRepository<RealizationStatus, Long> {

    /**
     * Find realization status by name
     * @param name the status name to search for
     * @return optional realization status with matching name
     */
    Optional<RealizationStatus> findByName(String name);

    /**
     * Find realization statuses by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of realization statuses containing the name
     */
    @Query("SELECT r FROM RealizationStatus r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<RealizationStatus> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all realization statuses ordered by name
     * @return list of realization statuses ordered by name ascending
     */
    List<RealizationStatus> findAllByOrderByNameAsc();

    /**
     * Check if realization status exists by name
     * @param name the status name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find realization status by name (case insensitive)
     * @param name the status name to search for
     * @return optional realization status with matching name
     */
    @Query("SELECT r FROM RealizationStatus r WHERE LOWER(r.name) = LOWER(:name)")
    Optional<RealizationStatus> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active realization statuses
     * @return list of active realization statuses
     */
    @Query("SELECT r FROM RealizationStatus r WHERE r.active = true")
    List<RealizationStatus> findAllActive();

    /**
     * Count active realization statuses
     * @return number of active realization statuses
     */
    @Query("SELECT COUNT(r) FROM RealizationStatus r WHERE r.active = true")
    long countActive();
}