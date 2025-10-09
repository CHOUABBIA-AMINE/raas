package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.Locality;
import dz.mdn.raas.common.administration.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Locality entity operations
 * Manages locality/city data access and queries
 */
@Repository
public interface LocalityRepository extends JpaRepository<Locality, Long> {

    /**
     * Find locality by name
     * @param name the locality name to search for
     * @return optional locality with matching name
     */
    Optional<Locality> findByName(String name);

    /**
     * Find locality by code
     * @param code the locality code to search for
     * @return optional locality with matching code
     */
    Optional<Locality> findByCode(String code);

    /**
     * Find localities by state
     * @param state the state to filter by
     * @return list of localities in the state
     */
    List<Locality> findByState(State state);

    /**
     * Find localities by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of localities containing the name
     */
    @Query("SELECT l FROM Locality l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Locality> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find localities by state ordered by name
     * @param state the state to filter by
     * @return list of localities ordered by name ascending
     */
    List<Locality> findByStateOrderByNameAsc(State state);

    /**
     * Find all localities ordered by name
     * @return list of localities ordered by name ascending
     */
    List<Locality> findAllByOrderByNameAsc();

    /**
     * Check if locality exists by name
     * @param name the locality name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if locality exists by code
     * @param code the locality code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Find locality by name (case insensitive)
     * @param name the locality name to search for
     * @return optional locality with matching name
     */
    @Query("SELECT l FROM Locality l WHERE LOWER(l.name) = LOWER(:name)")
    Optional<Locality> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find locality by code (case insensitive)
     * @param code the locality code to search for
     * @return optional locality with matching code
     */
    @Query("SELECT l FROM Locality l WHERE LOWER(l.code) = LOWER(:code)")
    Optional<Locality> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active localities
     * @return list of active localities
     */
    @Query("SELECT l FROM Locality l WHERE l.active = true")
    List<Locality> findAllActive();

    /**
     * Find active localities by state
     * @param state the state to filter by
     * @return list of active localities in the state
     */
    @Query("SELECT l FROM Locality l WHERE l.state = :state AND l.active = true")
    List<Locality> findActiveByState(@Param("state") State state);

    /**
     * Count localities by state
     * @param state the state to count localities for
     * @return count of localities in the state
     */
    long countByState(State state);

    /**
     * Count active localities
     * @return number of active localities
     */
    @Query("SELECT COUNT(l) FROM Locality l WHERE l.active = true")
    long countActive();
}