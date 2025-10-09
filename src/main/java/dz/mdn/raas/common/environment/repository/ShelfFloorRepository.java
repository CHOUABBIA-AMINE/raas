package dz.mdn.raas.common.environment.repository;

import dz.mdn.raas.common.environment.model.Shelf;
import dz.mdn.raas.common.environment.model.ShelfFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ShelfFloor entity operations
 * Manages shelf floor level data access and queries
 */
@Repository
public interface ShelfFloorRepository extends JpaRepository<ShelfFloor, Long> {

    /**
     * Find shelf floor by name
     * @param name the shelf floor name to search for
     * @return optional shelf floor with matching name
     */
    Optional<ShelfFloor> findByName(String name);

    /**
     * Find shelf floors by shelf
     * @param shelf the shelf to filter by
     * @return list of shelf floors on the shelf
     */
    List<ShelfFloor> findByShelf(Shelf shelf);

    /**
     * Find shelf floors by level
     * @param level the level number to filter by
     * @return list of shelf floors at the level
     */
    List<ShelfFloor> findByLevel(Integer level);

    /**
     * Find shelf floors by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of shelf floors containing the name
     */
    @Query("SELECT sf FROM ShelfFloor sf WHERE LOWER(sf.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ShelfFloor> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find shelf floors by shelf ordered by level
     * @param shelf the shelf to filter by
     * @return list of shelf floors ordered by level ascending
     */
    List<ShelfFloor> findByShelfOrderByLevelAsc(Shelf shelf);

    /**
     * Find shelf floors by shelf ordered by name
     * @param shelf the shelf to filter by
     * @return list of shelf floors ordered by name ascending
     */
    List<ShelfFloor> findByShelfOrderByNameAsc(Shelf shelf);

    /**
     * Find all shelf floors ordered by level
     * @return list of shelf floors ordered by level ascending
     */
    List<ShelfFloor> findAllByOrderByLevelAsc();

    /**
     * Check if shelf floor exists by name
     * @param name the shelf floor name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find shelf floor by name (case insensitive)
     * @param name the shelf floor name to search for
     * @return optional shelf floor with matching name
     */
    @Query("SELECT sf FROM ShelfFloor sf WHERE LOWER(sf.name) = LOWER(:name)")
    Optional<ShelfFloor> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find shelf floors by shelf and level
     * @param shelf the shelf to filter by
     * @param level the level to filter by
     * @return list of shelf floors matching both criteria
     */
    List<ShelfFloor> findByShelfAndLevel(Shelf shelf, Integer level);

    /**
     * Find all active shelf floors
     * @return list of active shelf floors
     */
    @Query("SELECT sf FROM ShelfFloor sf WHERE sf.active = true")
    List<ShelfFloor> findAllActive();

    /**
     * Find active shelf floors by shelf
     * @param shelf the shelf to filter by
     * @return list of active shelf floors on the shelf
     */
    @Query("SELECT sf FROM ShelfFloor sf WHERE sf.shelf = :shelf AND sf.active = true")
    List<ShelfFloor> findActiveByShelf(@Param("shelf") Shelf shelf);

    /**
     * Count shelf floors by shelf
     * @param shelf the shelf to count floors for
     * @return count of shelf floors on the shelf
     */
    long countByShelf(Shelf shelf);

    /**
     * Count shelf floors by level
     * @param level the level to count floors for
     * @return count of shelf floors at the level
     */
    long countByLevel(Integer level);

    /**
     * Count active shelf floors
     * @return number of active shelf floors
     */
    @Query("SELECT COUNT(sf) FROM ShelfFloor sf WHERE sf.active = true")
    long countActive();
}