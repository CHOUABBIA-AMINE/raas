package dz.mdn.raas.bussiness.amendment.repository;

import dz.mdn.raas.bussiness.amendment.model.AmendmentPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AmendmentPhase entity operations
 * Manages amendment phase data access and queries
 */
@Repository
public interface AmendmentPhaseRepository extends JpaRepository<AmendmentPhase, Long> {

    /**
     * Find amendment phase by name
     * @param name the phase name to search for
     * @return optional amendment phase with matching name
     */
    Optional<AmendmentPhase> findByName(String name);

    /**
     * Find amendment phases by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of amendment phases containing the name
     */
    @Query("SELECT ap FROM AmendmentPhase ap WHERE LOWER(ap.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AmendmentPhase> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all amendment phases ordered by name
     * @return list of amendment phases ordered by name ascending
     */
    List<AmendmentPhase> findAllByOrderByNameAsc();

    /**
     * Check if amendment phase exists by name
     * @param name the phase name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find amendment phase by name (case insensitive)
     * @param name the phase name to search for
     * @return optional amendment phase with matching name
     */
    @Query("SELECT ap FROM AmendmentPhase ap WHERE LOWER(ap.name) = LOWER(:name)")
    Optional<AmendmentPhase> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active amendment phases
     * @return list of active amendment phases
     */
    @Query("SELECT ap FROM AmendmentPhase ap WHERE ap.active = true")
    List<AmendmentPhase> findAllActive();

    /**
     * Count active amendment phases
     * @return number of active amendment phases
     */
    @Query("SELECT COUNT(ap) FROM AmendmentPhase ap WHERE ap.active = true")
    long countActive();
}