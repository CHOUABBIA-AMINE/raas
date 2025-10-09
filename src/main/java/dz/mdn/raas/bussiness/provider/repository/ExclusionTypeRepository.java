package dz.mdn.raas.bussiness.provider.repository;

import dz.mdn.raas.bussiness.provider.model.ExclusionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ExclusionType entity operations
 * Manages exclusion type data access and queries
 */
@Repository
public interface ExclusionTypeRepository extends JpaRepository<ExclusionType, Long> {

    /**
     * Find exclusion type by name
     * @param name the type name to search for
     * @return optional exclusion type with matching name
     */
    Optional<ExclusionType> findByName(String name);

    /**
     * Find exclusion types by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of exclusion types containing the name
     */
    @Query("SELECT et FROM ExclusionType et WHERE LOWER(et.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ExclusionType> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all exclusion types ordered by name
     * @return list of exclusion types ordered by name ascending
     */
    List<ExclusionType> findAllByOrderByNameAsc();

    /**
     * Check if exclusion type exists by name
     * @param name the type name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find exclusion type by name (case insensitive)
     * @param name the type name to search for
     * @return optional exclusion type with matching name
     */
    @Query("SELECT et FROM ExclusionType et WHERE LOWER(et.name) = LOWER(:name)")
    Optional<ExclusionType> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active exclusion types
     * @return list of active exclusion types
     */
    @Query("SELECT et FROM ExclusionType et WHERE et.active = true")
    List<ExclusionType> findAllActive();

    /**
     * Count active exclusion types
     * @return number of active exclusion types
     */
    @Query("SELECT COUNT(et) FROM ExclusionType et WHERE et.active = true")
    long countActive();
}