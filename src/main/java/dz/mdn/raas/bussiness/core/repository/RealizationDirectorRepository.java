package dz.mdn.raas.bussiness.core.repository;

import dz.mdn.raas.bussiness.core.model.RealizationDirector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RealizationDirector entity operations
 * Manages realization director data access and queries
 */
@Repository
public interface RealizationDirectorRepository extends JpaRepository<RealizationDirector, Long> {

    /**
     * Find realization director by name
     * @param name the director name to search for
     * @return optional realization director with matching name
     */
    Optional<RealizationDirector> findByName(String name);

    /**
     * Find realization directors by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of realization directors containing the name
     */
    @Query("SELECT r FROM RealizationDirector r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<RealizationDirector> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all realization directors ordered by name
     * @return list of realization directors ordered by name ascending
     */
    List<RealizationDirector> findAllByOrderByNameAsc();

    /**
     * Check if realization director exists by name
     * @param name the director name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find realization director by name (case insensitive)
     * @param name the director name to search for
     * @return optional realization director with matching name
     */
    @Query("SELECT r FROM RealizationDirector r WHERE LOWER(r.name) = LOWER(:name)")
    Optional<RealizationDirector> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active realization directors
     * @return list of active realization directors
     */
    @Query("SELECT r FROM RealizationDirector r WHERE r.active = true")
    List<RealizationDirector> findAllActive();

    /**
     * Count active realization directors
     * @return number of active realization directors
     */
    @Query("SELECT COUNT(r) FROM RealizationDirector r WHERE r.active = true")
    long countActive();
}