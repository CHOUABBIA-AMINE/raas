package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.StructureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for StructureType entity operations
 * Manages structure type data access and queries
 */
@Repository
public interface StructureTypeRepository extends JpaRepository<StructureType, Long> {

    /**
     * Find structure type by name
     * @param name the type name to search for
     * @return optional structure type with matching name
     */
    Optional<StructureType> findByName(String name);

    /**
     * Find structure types by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of structure types containing the name
     */
    @Query("SELECT st FROM StructureType st WHERE LOWER(st.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<StructureType> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all structure types ordered by name
     * @return list of structure types ordered by name ascending
     */
    List<StructureType> findAllByOrderByNameAsc();

    /**
     * Check if structure type exists by name
     * @param name the type name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find structure type by name (case insensitive)
     * @param name the type name to search for
     * @return optional structure type with matching name
     */
    @Query("SELECT st FROM StructureType st WHERE LOWER(st.name) = LOWER(:name)")
    Optional<StructureType> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active structure types
     * @return list of active structure types
     */
    @Query("SELECT st FROM StructureType st WHERE st.active = true")
    List<StructureType> findAllActive();

    /**
     * Count active structure types
     * @return number of active structure types
     */
    @Query("SELECT COUNT(st) FROM StructureType st WHERE st.active = true")
    long countActive();
}