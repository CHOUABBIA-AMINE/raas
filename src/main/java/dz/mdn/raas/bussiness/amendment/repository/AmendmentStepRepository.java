package dz.mdn.raas.bussiness.amendment.repository;

import dz.mdn.raas.bussiness.amendment.model.AmendmentStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AmendmentStep entity operations
 * Manages amendment step data access and queries
 */
@Repository
public interface AmendmentStepRepository extends JpaRepository<AmendmentStep, Long> {

    /**
     * Find amendment step by name
     * @param name the step name to search for
     * @return optional amendment step with matching name
     */
    Optional<AmendmentStep> findByName(String name);

    /**
     * Find amendment steps by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of amendment steps containing the name
     */
    @Query("SELECT as FROM AmendmentStep as WHERE LOWER(as.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AmendmentStep> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all amendment steps ordered by name
     * @return list of amendment steps ordered by name ascending
     */
    List<AmendmentStep> findAllByOrderByNameAsc();

    /**
     * Check if amendment step exists by name
     * @param name the step name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find amendment step by name (case insensitive)
     * @param name the step name to search for
     * @return optional amendment step with matching name
     */
    @Query("SELECT as FROM AmendmentStep as WHERE LOWER(as.name) = LOWER(:name)")
    Optional<AmendmentStep> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active amendment steps
     * @return list of active amendment steps
     */
    @Query("SELECT as FROM AmendmentStep as WHERE as.active = true")
    List<AmendmentStep> findAllActive();

    /**
     * Count active amendment steps
     * @return number of active amendment steps
     */
    @Query("SELECT COUNT(as) FROM AmendmentStep as WHERE as.active = true")
    long countActive();
}