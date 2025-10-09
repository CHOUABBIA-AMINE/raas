package dz.mdn.raas.bussiness.consultation.repository;

import dz.mdn.raas.bussiness.consultation.model.AwardMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AwardMethod entity operations
 * Manages award method data access and queries
 */
@Repository
public interface AwardMethodRepository extends JpaRepository<AwardMethod, Long> {

    /**
     * Find award method by name
     * @param name the method name to search for
     * @return optional award method with matching name
     */
    Optional<AwardMethod> findByName(String name);

    /**
     * Find award methods by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of award methods containing the name
     */
    @Query("SELECT a FROM AwardMethod a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AwardMethod> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all award methods ordered by name
     * @return list of award methods ordered by name ascending
     */
    List<AwardMethod> findAllByOrderByNameAsc();

    /**
     * Check if award method exists by name
     * @param name the method name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find award method by name (case insensitive)
     * @param name the method name to search for
     * @return optional award method with matching name
     */
    @Query("SELECT a FROM AwardMethod a WHERE LOWER(a.name) = LOWER(:name)")
    Optional<AwardMethod> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active award methods
     * @return list of active award methods
     */
    @Query("SELECT a FROM AwardMethod a WHERE a.active = true")
    List<AwardMethod> findAllActive();

    /**
     * Count active award methods
     * @return number of active award methods
     */
    @Query("SELECT COUNT(a) FROM AwardMethod a WHERE a.active = true")
    long countActive();
}