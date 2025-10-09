package dz.mdn.raas.bussiness.consultation.repository;

import dz.mdn.raas.bussiness.consultation.model.ConsultationStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ConsultationStep entity operations
 * Manages consultation step data access and queries
 */
@Repository
public interface ConsultationStepRepository extends JpaRepository<ConsultationStep, Long> {

    /**
     * Find consultation step by name
     * @param name the step name to search for
     * @return optional consultation step with matching name
     */
    Optional<ConsultationStep> findByName(String name);

    /**
     * Find consultation steps by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of consultation steps containing the name
     */
    @Query("SELECT c FROM ConsultationStep c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ConsultationStep> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all consultation steps ordered by name
     * @return list of consultation steps ordered by name ascending
     */
    List<ConsultationStep> findAllByOrderByNameAsc();

    /**
     * Check if consultation step exists by name
     * @param name the step name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find consultation step by name (case insensitive)
     * @param name the step name to search for
     * @return optional consultation step with matching name
     */
    @Query("SELECT c FROM ConsultationStep c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<ConsultationStep> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active consultation steps
     * @return list of active consultation steps
     */
    @Query("SELECT c FROM ConsultationStep c WHERE c.active = true")
    List<ConsultationStep> findAllActive();

    /**
     * Count active consultation steps
     * @return number of active consultation steps
     */
    @Query("SELECT COUNT(c) FROM ConsultationStep c WHERE c.active = true")
    long countActive();
}