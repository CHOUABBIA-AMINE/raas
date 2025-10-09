package dz.mdn.raas.bussiness.core.repository;

import dz.mdn.raas.bussiness.core.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Currency entity operations
 * Manages currency data access and queries
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * Find currency by code (e.g., USD, EUR, DZD)
     * @param code the currency code to search for
     * @return optional currency with matching code
     */
    Optional<Currency> findByCode(String code);

    /**
     * Find currency by name
     * @param name the currency name to search for
     * @return optional currency with matching name
     */
    Optional<Currency> findByName(String name);

    /**
     * Find currencies by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of currencies containing the name
     */
    @Query("SELECT c FROM Currency c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Currency> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all active currencies
     * @return list of active currencies
     */
    @Query("SELECT c FROM Currency c WHERE c.active = true")
    List<Currency> findAllActive();

    /**
     * Find all currencies ordered by code
     * @return list of currencies ordered by code ascending
     */
    List<Currency> findAllByOrderByCodeAsc();

    /**
     * Find all currencies ordered by name
     * @return list of currencies ordered by name ascending
     */
    List<Currency> findAllByOrderByNameAsc();

    /**
     * Check if currency exists by code
     * @param code the currency code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Check if currency exists by name
     * @param name the currency name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find currency by code (case insensitive)
     * @param code the currency code to search for
     * @return optional currency with matching code
     */
    @Query("SELECT c FROM Currency c WHERE LOWER(c.code) = LOWER(:code)")
    Optional<Currency> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Count active currencies
     * @return number of active currencies
     */
    @Query("SELECT COUNT(c) FROM Currency c WHERE c.active = true")
    long countActive();
}