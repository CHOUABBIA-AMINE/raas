package dz.mdn.raas.common.environment.repository;

import dz.mdn.raas.common.environment.model.Bloc;
import dz.mdn.raas.common.environment.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Floor entity operations
 * Manages building floor data access and queries
 */
@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {

    /**
     * Find floor by name
     * @param name the floor name to search for
     * @return optional floor with matching name
     */
    Optional<Floor> findByName(String name);

    /**
     * Find floors by bloc
     * @param bloc the bloc to filter by
     * @return list of floors in the bloc
     */
    List<Floor> findByBloc(Bloc bloc);

    /**
     * Find floors by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of floors containing the name
     */
    @Query("SELECT f FROM Floor f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Floor> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find floors by bloc ordered by name
     * @param bloc the bloc to filter by
     * @return list of floors ordered by name ascending
     */
    List<Floor> findByBlocOrderByNameAsc(Bloc bloc);

    /**
     * Find all floors ordered by name
     * @return list of floors ordered by name ascending
     */
    List<Floor> findAllByOrderByNameAsc();

    /**
     * Check if floor exists by name
     * @param name the floor name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find floor by name (case insensitive)
     * @param name the floor name to search for
     * @return optional floor with matching name
     */
    @Query("SELECT f FROM Floor f WHERE LOWER(f.name) = LOWER(:name)")
    Optional<Floor> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active floors
     * @return list of active floors
     */
    @Query("SELECT f FROM Floor f WHERE f.active = true")
    List<Floor> findAllActive();

    /**
     * Find active floors by bloc
     * @param bloc the bloc to filter by
     * @return list of active floors in the bloc
     */
    @Query("SELECT f FROM Floor f WHERE f.bloc = :bloc AND f.active = true")
    List<Floor> findActiveByBloc(@Param("bloc") Bloc bloc);

    /**
     * Count floors by bloc
     * @param bloc the bloc to count floors for
     * @return count of floors in the bloc
     */
    long countByBloc(Bloc bloc);

    /**
     * Count active floors
     * @return number of active floors
     */
    @Query("SELECT COUNT(f) FROM Floor f WHERE f.active = true")
    long countActive();
}