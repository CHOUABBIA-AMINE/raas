package dz.mdn.raas.bussiness.plan.repository;

import dz.mdn.raas.bussiness.plan.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Domain entity operations
 * Manages domain data access and queries
 */
@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {

    /**
     * Find domain by name
     * @param name the domain name to search for
     * @return optional domain with matching name
     */
    Optional<Domain> findByName(String name);

    /**
     * Find domains by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of domains containing the name
     */
    @Query("SELECT d FROM Domain d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Domain> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all domains ordered by name
     * @return list of domains ordered by name ascending
     */
    List<Domain> findAllByOrderByNameAsc();

    /**
     * Check if domain exists by name
     * @param name the domain name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find domain by name (case insensitive)
     * @param name the domain name to search for
     * @return optional domain with matching name
     */
    @Query("SELECT d FROM Domain d WHERE LOWER(d.name) = LOWER(:name)")
    Optional<Domain> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active domains
     * @return list of active domains
     */
    @Query("SELECT d FROM Domain d WHERE d.active = true")
    List<Domain> findAllActive();

    /**
     * Count active domains
     * @return number of active domains
     */
    @Query("SELECT COUNT(d) FROM Domain d WHERE d.active = true")
    long countActive();
}