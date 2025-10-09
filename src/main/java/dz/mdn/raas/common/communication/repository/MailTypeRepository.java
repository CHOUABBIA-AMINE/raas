package dz.mdn.raas.common.communication.repository;

import dz.mdn.raas.common.communication.model.MailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MailType entity operations
 * Manages mail type data access and queries
 */
@Repository
public interface MailTypeRepository extends JpaRepository<MailType, Long> {

    /**
     * Find mail type by name
     * @param name the type name to search for
     * @return optional mail type with matching name
     */
    Optional<MailType> findByName(String name);

    /**
     * Find mail types by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of mail types containing the name
     */
    @Query("SELECT mt FROM MailType mt WHERE LOWER(mt.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MailType> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all mail types ordered by name
     * @return list of mail types ordered by name ascending
     */
    List<MailType> findAllByOrderByNameAsc();

    /**
     * Check if mail type exists by name
     * @param name the type name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find mail type by name (case insensitive)
     * @param name the type name to search for
     * @return optional mail type with matching name
     */
    @Query("SELECT mt FROM MailType mt WHERE LOWER(mt.name) = LOWER(:name)")
    Optional<MailType> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active mail types
     * @return list of active mail types
     */
    @Query("SELECT mt FROM MailType mt WHERE mt.active = true")
    List<MailType> findAllActive();

    /**
     * Count active mail types
     * @return number of active mail types
     */
    @Query("SELECT COUNT(mt) FROM MailType mt WHERE mt.active = true")
    long countActive();
}