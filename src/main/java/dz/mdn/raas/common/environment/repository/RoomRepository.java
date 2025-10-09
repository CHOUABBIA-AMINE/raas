package dz.mdn.raas.common.environment.repository;

import dz.mdn.raas.common.environment.model.Floor;
import dz.mdn.raas.common.environment.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Room entity operations
 * Manages room data access and queries
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Find room by name
     * @param name the room name to search for
     * @return optional room with matching name
     */
    Optional<Room> findByName(String name);

    /**
     * Find room by number
     * @param number the room number to search for
     * @return optional room with matching number
     */
    Optional<Room> findByNumber(String number);

    /**
     * Find rooms by floor
     * @param floor the floor to filter by
     * @return list of rooms on the floor
     */
    List<Room> findByFloor(Floor floor);

    /**
     * Find rooms by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of rooms containing the name
     */
    @Query("SELECT r FROM Room r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Room> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find rooms by number containing (case insensitive)
     * @param number the partial number to search for
     * @return list of rooms containing the number
     */
    @Query("SELECT r FROM Room r WHERE LOWER(r.number) LIKE LOWER(CONCAT('%', :number, '%'))")
    List<Room> findByNumberContainingIgnoreCase(@Param("number") String number);

    /**
     * Find rooms by floor ordered by number
     * @param floor the floor to filter by
     * @return list of rooms ordered by number ascending
     */
    List<Room> findByFloorOrderByNumberAsc(Floor floor);

    /**
     * Find rooms by floor ordered by name
     * @param floor the floor to filter by
     * @return list of rooms ordered by name ascending
     */
    List<Room> findByFloorOrderByNameAsc(Floor floor);

    /**
     * Find all rooms ordered by number
     * @return list of rooms ordered by number ascending
     */
    List<Room> findAllByOrderByNumberAsc();

    /**
     * Check if room exists by name
     * @param name the room name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if room exists by number
     * @param number the room number to check
     * @return true if exists, false otherwise
     */
    boolean existsByNumber(String number);

    /**
     * Find room by name (case insensitive)
     * @param name the room name to search for
     * @return optional room with matching name
     */
    @Query("SELECT r FROM Room r WHERE LOWER(r.name) = LOWER(:name)")
    Optional<Room> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find room by number (case insensitive)
     * @param number the room number to search for
     * @return optional room with matching number
     */
    @Query("SELECT r FROM Room r WHERE LOWER(r.number) = LOWER(:number)")
    Optional<Room> findByNumberIgnoreCase(@Param("number") String number);

    /**
     * Find all active rooms
     * @return list of active rooms
     */
    @Query("SELECT r FROM Room r WHERE r.active = true")
    List<Room> findAllActive();

    /**
     * Find active rooms by floor
     * @param floor the floor to filter by
     * @return list of active rooms on the floor
     */
    @Query("SELECT r FROM Room r WHERE r.floor = :floor AND r.active = true")
    List<Room> findActiveByFloor(@Param("floor") Floor floor);

    /**
     * Count rooms by floor
     * @param floor the floor to count rooms for
     * @return count of rooms on the floor
     */
    long countByFloor(Floor floor);

    /**
     * Count active rooms
     * @return number of active rooms
     */
    @Query("SELECT COUNT(r) FROM Room r WHERE r.active = true")
    long countActive();
}