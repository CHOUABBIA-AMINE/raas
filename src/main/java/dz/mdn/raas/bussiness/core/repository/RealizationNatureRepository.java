package dz.mdn.raas.bussiness.core.repository;

import dz.mdn.raas.bussiness.core.model.RealizationNature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RealizationNature entity operations
 * Manages realization nature data access and queries
 */
@Repository
public interface RealizationNatureRepository extends JpaRepository<RealizationNature, Long> {

    /**
     * Find realization nature by name
     * @param name the nature name to search for
     * @return optional realization nature with matching name
     */
    Optional<RealizationNature> findByName(String name);

    /**
     * Find realization natures by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of realization natures containing the name
     */
    @Query("SELECT r FROM RealizationNature r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<RealizationNature> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all realization natures ordered by name
     * @return list of realization natures ordered by name ascending
     */
    List<RealizationNature> findAllByOrderByNameAsc();

    /**
     * Check if realization nature exists by name
     * @param name the nature name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find realization nature by name (case insensitive)
     * @param name the nature name to search for
     * @return optional realization nature with matching name
     */
    @Query("SELECT r FROM RealizationNature r WHERE LOWER(r.name) = LOWER(:name)")
    Optional<RealizationNature> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active realization natures
     * @return list of active realization natures
     */
    @Query("SELECT r FROM RealizationNature r WHERE r.active = true")
    List<RealizationNature> findAllActive();

    /**
     * Count active realization natures
     * @return number of active realization natures
     */
    @Query("SELECT COUNT(r) FROM RealizationNature r WHERE r.active = true")
    long countActive();
}