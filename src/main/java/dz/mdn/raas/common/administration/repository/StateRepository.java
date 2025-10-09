package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.Country;
import dz.mdn.raas.common.administration.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for State entity operations
 * Manages state/province data access and queries
 */
@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    /**
     * Find state by name
     * @param name the state name to search for
     * @return optional state with matching name
     */
    Optional<State> findByName(String name);

    /**
     * Find state by code
     * @param code the state code to search for
     * @return optional state with matching code
     */
    Optional<State> findByCode(String code);

    /**
     * Find states by country
     * @param country the country to filter by
     * @return list of states in the country
     */
    List<State> findByCountry(Country country);

    /**
     * Find states by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of states containing the name
     */
    @Query("SELECT s FROM State s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<State> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find states by country ordered by name
     * @param country the country to filter by
     * @return list of states ordered by name ascending
     */
    List<State> findByCountryOrderByNameAsc(Country country);

    /**
     * Find all states ordered by name
     * @return list of states ordered by name ascending
     */
    List<State> findAllByOrderByNameAsc();

    /**
     * Check if state exists by name
     * @param name the state name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if state exists by code
     * @param code the state code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Find state by name (case insensitive)
     * @param name the state name to search for
     * @return optional state with matching name
     */
    @Query("SELECT s FROM State s WHERE LOWER(s.name) = LOWER(:name)")
    Optional<State> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find state by code (case insensitive)
     * @param code the state code to search for
     * @return optional state with matching code
     */
    @Query("SELECT s FROM State s WHERE LOWER(s.code) = LOWER(:code)")
    Optional<State> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active states
     * @return list of active states
     */
    @Query("SELECT s FROM State s WHERE s.active = true")
    List<State> findAllActive();

    /**
     * Find active states by country
     * @param country the country to filter by
     * @return list of active states in the country
     */
    @Query("SELECT s FROM State s WHERE s.country = :country AND s.active = true")
    List<State> findActiveByCountry(@Param("country") Country country);

    /**
     * Count states by country
     * @param country the country to count states for
     * @return count of states in the country
     */
    long countByCountry(Country country);

    /**
     * Count active states
     * @return number of active states
     */
    @Query("SELECT COUNT(s) FROM State s WHERE s.active = true")
    long countActive();
}