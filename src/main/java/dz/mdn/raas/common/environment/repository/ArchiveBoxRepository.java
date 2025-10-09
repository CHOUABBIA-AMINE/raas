package dz.mdn.raas.common.environment.repository;

import dz.mdn.raas.common.environment.model.ArchiveBox;
import dz.mdn.raas.common.environment.model.ShelfFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ArchiveBox entity operations
 * Manages archive box data access and queries
 */
@Repository
public interface ArchiveBoxRepository extends JpaRepository<ArchiveBox, Long> {

    /**
     * Find archive box by name
     * @param name the box name to search for
     * @return optional archive box with matching name
     */
    Optional<ArchiveBox> findByName(String name);

    /**
     * Find archive box by code
     * @param code the box code to search for
     * @return optional archive box with matching code
     */
    Optional<ArchiveBox> findByCode(String code);

    /**
     * Find archive boxes by shelf floor
     * @param shelfFloor the shelf floor to filter by
     * @return list of archive boxes on the shelf floor
     */
    List<ArchiveBox> findByShelfFloor(ShelfFloor shelfFloor);

    /**
     * Find archive boxes by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of archive boxes containing the name
     */
    @Query("SELECT ab FROM ArchiveBox ab WHERE LOWER(ab.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ArchiveBox> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find archive boxes by code containing (case insensitive)
     * @param code the partial code to search for
     * @return list of archive boxes containing the code
     */
    @Query("SELECT ab FROM ArchiveBox ab WHERE LOWER(ab.code) LIKE LOWER(CONCAT('%', :code, '%'))")
    List<ArchiveBox> findByCodeContainingIgnoreCase(@Param("code") String code);

    /**
     * Find archive boxes by shelf floor ordered by code
     * @param shelfFloor the shelf floor to filter by
     * @return list of archive boxes ordered by code ascending
     */
    List<ArchiveBox> findByShelfFloorOrderByCodeAsc(ShelfFloor shelfFloor);

    /**
     * Find archive boxes by shelf floor ordered by name
     * @param shelfFloor the shelf floor to filter by
     * @return list of archive boxes ordered by name ascending
     */
    List<ArchiveBox> findByShelfFloorOrderByNameAsc(ShelfFloor shelfFloor);

    /**
     * Find all archive boxes ordered by code
     * @return list of archive boxes ordered by code ascending
     */
    List<ArchiveBox> findAllByOrderByCodeAsc();

    /**
     * Check if archive box exists by name
     * @param name the box name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if archive box exists by code
     * @param code the box code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Find archive box by name (case insensitive)
     * @param name the box name to search for
     * @return optional archive box with matching name
     */
    @Query("SELECT ab FROM ArchiveBox ab WHERE LOWER(ab.name) = LOWER(:name)")
    Optional<ArchiveBox> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find archive box by code (case insensitive)
     * @param code the box code to search for
     * @return optional archive box with matching code
     */
    @Query("SELECT ab FROM ArchiveBox ab WHERE LOWER(ab.code) = LOWER(:code)")
    Optional<ArchiveBox> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active archive boxes
     * @return list of active archive boxes
     */
    @Query("SELECT ab FROM ArchiveBox ab WHERE ab.active = true")
    List<ArchiveBox> findAllActive();

    /**
     * Find active archive boxes by shelf floor
     * @param shelfFloor the shelf floor to filter by
     * @return list of active archive boxes on the shelf floor
     */
    @Query("SELECT ab FROM ArchiveBox ab WHERE ab.shelfFloor = :shelfFloor AND ab.active = true")
    List<ArchiveBox> findActiveByShelfFloor(@Param("shelfFloor") ShelfFloor shelfFloor);

    /**
     * Count archive boxes by shelf floor
     * @param shelfFloor the shelf floor to count boxes for
     * @return count of archive boxes on the shelf floor
     */
    long countByShelfFloor(ShelfFloor shelfFloor);

    /**
     * Count active archive boxes
     * @return number of active archive boxes
     */
    @Query("SELECT COUNT(ab) FROM ArchiveBox ab WHERE ab.active = true")
    long countActive();
}