package dz.mdn.raas.common.environment.repository;

import dz.mdn.raas.common.environment.model.Bloc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Bloc entity operations
 * Manages building bloc data access and queries
 */
@Repository
public interface BlocRepository extends JpaRepository<Bloc, Long> {

    /**
     * Find bloc by name
     * @param name the bloc name to search for
     * @return optional bloc with matching name
     */
    Optional<Bloc> findByName(String name);

    /**
     * Find bloc by code
     * @param code the bloc code to search for
     * @return optional bloc with matching code
     */
    Optional<Bloc> findByCode(String code);

    /**
     * Find blocs by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of blocs containing the name
     */
    @Query("SELECT b FROM Bloc b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Bloc> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all blocs ordered by name
     * @return list of blocs ordered by name ascending
     */
    List<Bloc> findAllByOrderByNameAsc();

    /**
     * Find all blocs ordered by code
     * @return list of blocs ordered by code ascending
     */
    List<Bloc> findAllByOrderByCodeAsc();

    /**
     * Check if bloc exists by name
     * @param name the bloc name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if bloc exists by code
     * @param code the bloc code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Find bloc by name (case insensitive)
     * @param name the bloc name to search for
     * @return optional bloc with matching name
     */
    @Query("SELECT b FROM Bloc b WHERE LOWER(b.name) = LOWER(:name)")
    Optional<Bloc> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find bloc by code (case insensitive)
     * @param code the bloc code to search for
     * @return optional bloc with matching code
     */
    @Query("SELECT b FROM Bloc b WHERE LOWER(b.code) = LOWER(:code)")
    Optional<Bloc> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active blocs
     * @return list of active blocs
     */
    @Query("SELECT b FROM Bloc b WHERE b.active = true")
    List<Bloc> findAllActive();

    /**
     * Count active blocs
     * @return number of active blocs
     */
    @Query("SELECT COUNT(b) FROM Bloc b WHERE b.active = true")
    long countActive();
}