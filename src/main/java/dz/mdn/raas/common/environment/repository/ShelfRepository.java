package dz.mdn.raas.common.environment.repository;

import dz.mdn.raas.common.environment.model.Room;
import dz.mdn.raas.common.environment.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Shelf entity operations
 * Manages shelf data access and queries
 */
@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {

    /**
     * Find shelf by name
     * @param name the shelf name to search for
     * @return optional shelf with matching name
     */
    Optional<Shelf> findByName(String name);

    /**
     * Find shelf by code
     * @param code the shelf code to search for
     * @return optional shelf with matching code
     */
    Optional<Shelf> findByCode(String code);

    /**
     * Find shelves by room
     * @param room the room to filter by
     * @return list of shelves in the room
     */
    List<Shelf> findByRoom(Room room);

    /**
     * Find shelves by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of shelves containing the name
     */
    @Query("SELECT s FROM Shelf s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Shelf> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find shelves by code containing (case insensitive)
     * @param code the partial code to search for
     * @return list of shelves containing the code
     */
    @Query("SELECT s FROM Shelf s WHERE LOWER(s.code) LIKE LOWER(CONCAT('%', :code, '%'))")
    List<Shelf> findByCodeContainingIgnoreCase(@Param("code") String code);

    /**
     * Find shelves by room ordered by code
     * @param room the room to filter by
     * @return list of shelves ordered by code ascending
     */
    List<Shelf> findByRoomOrderByCodeAsc(Room room);

    /**
     * Find shelves by room ordered by name
     * @param room the room to filter by
     * @return list of shelves ordered by name ascending
     */
    List<Shelf> findByRoomOrderByNameAsc(Room room);

    /**
     * Find all shelves ordered by code
     * @return list of shelves ordered by code ascending
     */
    List<Shelf> findAllByOrderByCodeAsc();

    /**
     * Check if shelf exists by name
     * @param name the shelf name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if shelf exists by code
     * @param code the shelf code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Find shelf by name (case insensitive)
     * @param name the shelf name to search for
     * @return optional shelf with matching name
     */
    @Query("SELECT s FROM Shelf s WHERE LOWER(s.name) = LOWER(:name)")
    Optional<Shelf> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find shelf by code (case insensitive)
     * @param code the shelf code to search for
     * @return optional shelf with matching code
     */
    @Query("SELECT s FROM Shelf s WHERE LOWER(s.code) = LOWER(:code)")
    Optional<Shelf> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active shelves
     * @return list of active shelves
     */
    @Query("SELECT s FROM Shelf s WHERE s.active = true")
    List<Shelf> findAllActive();

    /**
     * Find active shelves by room
     * @param room the room to filter by
     * @return list of active shelves in the room
     */
    @Query("SELECT s FROM Shelf s WHERE s.room = :room AND s.active = true")
    List<Shelf> findActiveByRoom(@Param("room") Room room);

    /**
     * Count shelves by room
     * @param room the room to count shelves for
     * @return count of shelves in the room
     */
    long countByRoom(Room room);

    /**
     * Count active shelves
     * @return number of active shelves
     */
    @Query("SELECT COUNT(s) FROM Shelf s WHERE s.active = true")
    long countActive();
}