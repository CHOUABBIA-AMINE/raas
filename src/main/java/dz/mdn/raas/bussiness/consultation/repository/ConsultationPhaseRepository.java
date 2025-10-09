package dz.mdn.raas.bussiness.consultation.repository;

import dz.mdn.raas.bussiness.consultation.model.ConsultationPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ConsultationPhase entity operations
 * Manages consultation phase data access and queries
 */
@Repository
public interface ConsultationPhaseRepository extends JpaRepository<ConsultationPhase, Long> {

    /**
     * Find consultation phase by name
     * @param name the phase name to search for
     * @return optional consultation phase with matching name
     */
    Optional<ConsultationPhase> findByName(String name);

    /**
     * Find consultation phases by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of consultation phases containing the name
     */
    @Query("SELECT c FROM ConsultationPhase c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ConsultationPhase> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all consultation phases ordered by name
     * @return list of consultation phases ordered by name ascending
     */
    List<ConsultationPhase> findAllByOrderByNameAsc();

    /**
     * Check if consultation phase exists by name
     * @param name the phase name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find consultation phase by name (case insensitive)
     * @param name the phase name to search for
     * @return optional consultation phase with matching name
     */
    @Query("SELECT c FROM ConsultationPhase c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<ConsultationPhase> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active consultation phases
     * @return list of active consultation phases
     */
    @Query("SELECT c FROM ConsultationPhase c WHERE c.active = true")
    List<ConsultationPhase> findAllActive();

    /**
     * Count active consultation phases
     * @return number of active consultation phases
     */
    @Query("SELECT COUNT(c) FROM ConsultationPhase c WHERE c.active = true")
    long countActive();
}