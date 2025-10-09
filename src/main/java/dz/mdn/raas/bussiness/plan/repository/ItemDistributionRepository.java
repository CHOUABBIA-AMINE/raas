package dz.mdn.raas.bussiness.plan.repository;

import dz.mdn.raas.bussiness.plan.model.ItemDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ItemDistribution entity operations
 * Manages item distribution data access and queries
 */
@Repository
public interface ItemDistributionRepository extends JpaRepository<ItemDistribution, Long> {

    /**
     * Find item distribution by name
     * @param name the distribution name to search for
     * @return optional item distribution with matching name
     */
    Optional<ItemDistribution> findByName(String name);

    /**
     * Find item distributions by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of item distributions containing the name
     */
    @Query("SELECT id FROM ItemDistribution id WHERE LOWER(id.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ItemDistribution> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all item distributions ordered by name
     * @return list of item distributions ordered by name ascending
     */
    List<ItemDistribution> findAllByOrderByNameAsc();

    /**
     * Check if item distribution exists by name
     * @param name the distribution name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find item distribution by name (case insensitive)
     * @param name the distribution name to search for
     * @return optional item distribution with matching name
     */
    @Query("SELECT id FROM ItemDistribution id WHERE LOWER(id.name) = LOWER(:name)")
    Optional<ItemDistribution> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active item distributions
     * @return list of active item distributions
     */
    @Query("SELECT id FROM ItemDistribution id WHERE id.active = true")
    List<ItemDistribution> findAllActive();

    /**
     * Count active item distributions
     * @return number of active item distributions
     */
    @Query("SELECT COUNT(id) FROM ItemDistribution id WHERE id.active = true")
    long countActive();
}