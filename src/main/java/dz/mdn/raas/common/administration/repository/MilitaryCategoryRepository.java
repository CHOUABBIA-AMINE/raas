package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.MilitaryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MilitaryCategory entity operations
 * Manages military category data access and queries
 */
@Repository
public interface MilitaryCategoryRepository extends JpaRepository<MilitaryCategory, Long> {

    /**
     * Find military category by name
     * @param name the category name to search for
     * @return optional military category with matching name
     */
    Optional<MilitaryCategory> findByName(String name);

    /**
     * Find military categories by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of military categories containing the name
     */
    @Query("SELECT mc FROM MilitaryCategory mc WHERE LOWER(mc.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MilitaryCategory> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all military categories ordered by name
     * @return list of military categories ordered by name ascending
     */
    List<MilitaryCategory> findAllByOrderByNameAsc();

    /**
     * Check if military category exists by name
     * @param name the category name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find military category by name (case insensitive)
     * @param name the category name to search for
     * @return optional military category with matching name
     */
    @Query("SELECT mc FROM MilitaryCategory mc WHERE LOWER(mc.name) = LOWER(:name)")
    Optional<MilitaryCategory> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active military categories
     * @return list of active military categories
     */
    @Query("SELECT mc FROM MilitaryCategory mc WHERE mc.active = true")
    List<MilitaryCategory> findAllActive();

    /**
     * Count active military categories
     * @return number of active military categories
     */
    @Query("SELECT COUNT(mc) FROM MilitaryCategory mc WHERE mc.active = true")
    long countActive();
}