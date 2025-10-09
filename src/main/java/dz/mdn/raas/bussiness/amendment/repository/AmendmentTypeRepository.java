package dz.mdn.raas.bussiness.amendment.repository;

import dz.mdn.raas.bussiness.amendment.model.AmendmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AmendmentType entity operations
 * Manages amendment type data access and queries
 */
@Repository
public interface AmendmentTypeRepository extends JpaRepository<AmendmentType, Long> {

    /**
     * Find amendment type by name
     * @param name the type name to search for
     * @return optional amendment type with matching name
     */
    Optional<AmendmentType> findByName(String name);

    /**
     * Find amendment types by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of amendment types containing the name
     */
    @Query("SELECT at FROM AmendmentType at WHERE LOWER(at.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AmendmentType> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all amendment types ordered by name
     * @return list of amendment types ordered by name ascending
     */
    List<AmendmentType> findAllByOrderByNameAsc();

    /**
     * Check if amendment type exists by name
     * @param name the type name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find amendment type by name (case insensitive)
     * @param name the type name to search for
     * @return optional amendment type with matching name
     */
    @Query("SELECT at FROM AmendmentType at WHERE LOWER(at.name) = LOWER(:name)")
    Optional<AmendmentType> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active amendment types
     * @return list of active amendment types
     */
    @Query("SELECT at FROM AmendmentType at WHERE at.active = true")
    List<AmendmentType> findAllActive();

    /**
     * Count active amendment types
     * @return number of active amendment types
     */
    @Query("SELECT COUNT(at) FROM AmendmentType at WHERE at.active = true")
    long countActive();
}