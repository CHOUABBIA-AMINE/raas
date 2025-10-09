package dz.mdn.raas.bussiness.provider.repository;

import dz.mdn.raas.bussiness.provider.model.EconomicNature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EconomicNature entity operations
 * Manages economic nature data access and queries
 */
@Repository
public interface EconomicNatureRepository extends JpaRepository<EconomicNature, Long> {

    /**
     * Find economic nature by name
     * @param name the nature name to search for
     * @return optional economic nature with matching name
     */
    Optional<EconomicNature> findByName(String name);

    /**
     * Find economic natures by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of economic natures containing the name
     */
    @Query("SELECT e FROM EconomicNature e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<EconomicNature> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all economic natures ordered by name
     * @return list of economic natures ordered by name ascending
     */
    List<EconomicNature> findAllByOrderByNameAsc();

    /**
     * Check if economic nature exists by name
     * @param name the nature name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find economic nature by name (case insensitive)
     * @param name the nature name to search for
     * @return optional economic nature with matching name
     */
    @Query("SELECT e FROM EconomicNature e WHERE LOWER(e.name) = LOWER(:name)")
    Optional<EconomicNature> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active economic natures
     * @return list of active economic natures
     */
    @Query("SELECT e FROM EconomicNature e WHERE e.active = true")
    List<EconomicNature> findAllActive();

    /**
     * Count active economic natures
     * @return number of active economic natures
     */
    @Query("SELECT COUNT(e) FROM EconomicNature e WHERE e.active = true")
    long countActive();
}