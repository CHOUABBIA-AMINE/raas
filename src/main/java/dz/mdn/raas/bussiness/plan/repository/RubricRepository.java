package dz.mdn.raas.bussiness.plan.repository;

import dz.mdn.raas.bussiness.plan.model.Rubric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Rubric entity operations
 * Manages rubric data access and queries
 */
@Repository
public interface RubricRepository extends JpaRepository<Rubric, Long> {

    /**
     * Find rubric by code
     * @param code the rubric code to search for
     * @return optional rubric with matching code
     */
    Optional<Rubric> findByCode(String code);

    /**
     * Find rubric by name
     * @param name the rubric name to search for
     * @return optional rubric with matching name
     */
    Optional<Rubric> findByName(String name);

    /**
     * Find rubrics by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of rubrics containing the name
     */
    @Query("SELECT r FROM Rubric r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Rubric> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all rubrics ordered by code
     * @return list of rubrics ordered by code ascending
     */
    List<Rubric> findAllByOrderByCodeAsc();

    /**
     * Find all rubrics ordered by name
     * @return list of rubrics ordered by name ascending
     */
    List<Rubric> findAllByOrderByNameAsc();

    /**
     * Check if rubric exists by code
     * @param code the rubric code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Check if rubric exists by name
     * @param name the rubric name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find rubric by code (case insensitive)
     * @param code the rubric code to search for
     * @return optional rubric with matching code
     */
    @Query("SELECT r FROM Rubric r WHERE LOWER(r.code) = LOWER(:code)")
    Optional<Rubric> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active rubrics
     * @return list of active rubrics
     */
    @Query("SELECT r FROM Rubric r WHERE r.active = true")
    List<Rubric> findAllActive();

    /**
     * Count active rubrics
     * @return number of active rubrics
     */
    @Query("SELECT COUNT(r) FROM Rubric r WHERE r.active = true")
    long countActive();
}