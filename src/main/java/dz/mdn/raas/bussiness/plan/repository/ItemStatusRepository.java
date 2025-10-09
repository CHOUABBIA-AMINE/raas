package dz.mdn.raas.bussiness.plan.repository;

import dz.mdn.raas.bussiness.plan.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ItemStatus entity operations
 * Manages item status data access and queries
 */
@Repository
public interface ItemStatusRepository extends JpaRepository<ItemStatus, Long> {

    /**
     * Find item status by name
     * @param name the status name to search for
     * @return optional item status with matching name
     */
    Optional<ItemStatus> findByName(String name);

    /**
     * Find item statuses by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of item statuses containing the name
     */
    @Query("SELECT is FROM ItemStatus is WHERE LOWER(is.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ItemStatus> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all item statuses ordered by name
     * @return list of item statuses ordered by name ascending
     */
    List<ItemStatus> findAllByOrderByNameAsc();

    /**
     * Check if item status exists by name
     * @param name the status name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find item status by name (case insensitive)
     * @param name the status name to search for
     * @return optional item status with matching name
     */
    @Query("SELECT is FROM ItemStatus is WHERE LOWER(is.name) = LOWER(:name)")
    Optional<ItemStatus> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active item statuses
     * @return list of active item statuses
     */
    @Query("SELECT is FROM ItemStatus is WHERE is.active = true")
    List<ItemStatus> findAllActive();

    /**
     * Count active item statuses
     * @return number of active item statuses
     */
    @Query("SELECT COUNT(is) FROM ItemStatus is WHERE is.active = true")
    long countActive();
}