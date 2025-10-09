package dz.mdn.raas.bussiness.provider.repository;

import dz.mdn.raas.bussiness.provider.model.EconomicDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EconomicDomain entity operations
 * Manages economic domain data access and queries
 */
@Repository
public interface EconomicDomainRepository extends JpaRepository<EconomicDomain, Long> {

    /**
     * Find economic domain by name
     * @param name the domain name to search for
     * @return optional economic domain with matching name
     */
    Optional<EconomicDomain> findByName(String name);

    /**
     * Find economic domains by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of economic domains containing the name
     */
    @Query("SELECT e FROM EconomicDomain e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<EconomicDomain> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all economic domains ordered by name
     * @return list of economic domains ordered by name ascending
     */
    List<EconomicDomain> findAllByOrderByNameAsc();

    /**
     * Check if economic domain exists by name
     * @param name the domain name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find economic domain by name (case insensitive)
     * @param name the domain name to search for
     * @return optional economic domain with matching name
     */
    @Query("SELECT e FROM EconomicDomain e WHERE LOWER(e.name) = LOWER(:name)")
    Optional<EconomicDomain> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active economic domains
     * @return list of active economic domains
     */
    @Query("SELECT e FROM EconomicDomain e WHERE e.active = true")
    List<EconomicDomain> findAllActive();

    /**
     * Count active economic domains
     * @return number of active economic domains
     */
    @Query("SELECT COUNT(e) FROM EconomicDomain e WHERE e.active = true")
    long countActive();
}