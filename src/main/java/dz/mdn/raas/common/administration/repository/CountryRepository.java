package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Country entity operations
 * Manages country data access and queries
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    /**
     * Find country by name
     * @param name the country name to search for
     * @return optional country with matching name
     */
    Optional<Country> findByName(String name);

    /**
     * Find country by code
     * @param code the country code to search for
     * @return optional country with matching code
     */
    Optional<Country> findByCode(String code);

    /**
     * Find countries by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of countries containing the name
     */
    @Query("SELECT c FROM Country c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Country> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all countries ordered by name
     * @return list of countries ordered by name ascending
     */
    List<Country> findAllByOrderByNameAsc();

    /**
     * Find all countries ordered by code
     * @return list of countries ordered by code ascending
     */
    List<Country> findAllByOrderByCodeAsc();

    /**
     * Check if country exists by name
     * @param name the country name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if country exists by code
     * @param code the country code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Find country by name (case insensitive)
     * @param name the country name to search for
     * @return optional country with matching name
     */
    @Query("SELECT c FROM Country c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<Country> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find country by code (case insensitive)
     * @param code the country code to search for
     * @return optional country with matching code
     */
    @Query("SELECT c FROM Country c WHERE LOWER(c.code) = LOWER(:code)")
    Optional<Country> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active countries
     * @return list of active countries
     */
    @Query("SELECT c FROM Country c WHERE c.active = true")
    List<Country> findAllActive();

    /**
     * Count active countries
     * @return number of active countries
     */
    @Query("SELECT COUNT(c) FROM Country c WHERE c.active = true")
    long countActive();
}