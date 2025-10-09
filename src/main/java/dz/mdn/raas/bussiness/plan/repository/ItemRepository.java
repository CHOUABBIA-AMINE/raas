package dz.mdn.raas.bussiness.plan.repository;

import dz.mdn.raas.bussiness.plan.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Item entity operations
 * Manages item data access and queries
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Find item by code
     * @param code the item code to search for
     * @return optional item with matching code
     */
    Optional<Item> findByCode(String code);

    /**
     * Find item by name
     * @param name the item name to search for
     * @return optional item with matching name
     */
    Optional<Item> findByName(String name);

    /**
     * Find items by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of items containing the name
     */
    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Item> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all items ordered by code
     * @return list of items ordered by code ascending
     */
    List<Item> findAllByOrderByCodeAsc();

    /**
     * Find all items ordered by name
     * @return list of items ordered by name ascending
     */
    List<Item> findAllByOrderByNameAsc();

    /**
     * Check if item exists by code
     * @param code the item code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Check if item exists by name
     * @param name the item name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find item by code (case insensitive)
     * @param code the item code to search for
     * @return optional item with matching code
     */
    @Query("SELECT i FROM Item i WHERE LOWER(i.code) = LOWER(:code)")
    Optional<Item> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active items
     * @return list of active items
     */
    @Query("SELECT i FROM Item i WHERE i.active = true")
    List<Item> findAllActive();

    /**
     * Count active items
     * @return number of active items
     */
    @Query("SELECT COUNT(i) FROM Item i WHERE i.active = true")
    long countActive();
}