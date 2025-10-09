package dz.mdn.raas.common.communication.repository;

import dz.mdn.raas.common.communication.model.MailNature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MailNature entity operations
 * Manages mail nature data access and queries
 */
@Repository
public interface MailNatureRepository extends JpaRepository<MailNature, Long> {

    /**
     * Find mail nature by name
     * @param name the nature name to search for
     * @return optional mail nature with matching name
     */
    Optional<MailNature> findByName(String name);

    /**
     * Find mail natures by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of mail natures containing the name
     */
    @Query("SELECT mn FROM MailNature mn WHERE LOWER(mn.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MailNature> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all mail natures ordered by name
     * @return list of mail natures ordered by name ascending
     */
    List<MailNature> findAllByOrderByNameAsc();

    /**
     * Check if mail nature exists by name
     * @param name the nature name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find mail nature by name (case insensitive)
     * @param name the nature name to search for
     * @return optional mail nature with matching name
     */
    @Query("SELECT mn FROM MailNature mn WHERE LOWER(mn.name) = LOWER(:name)")
    Optional<MailNature> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active mail natures
     * @return list of active mail natures
     */
    @Query("SELECT mn FROM MailNature mn WHERE mn.active = true")
    List<MailNature> findAllActive();

    /**
     * Count active mail natures
     * @return number of active mail natures
     */
    @Query("SELECT COUNT(mn) FROM MailNature mn WHERE mn.active = true")
    long countActive();
}