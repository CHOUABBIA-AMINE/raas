package dz.mdn.raas.common.environment.repository;

import dz.mdn.raas.common.environment.model.ArchiveBox;
import dz.mdn.raas.common.environment.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Folder entity operations
 * Manages folder data access and queries
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    /**
     * Find folder by name
     * @param name the folder name to search for
     * @return optional folder with matching name
     */
    Optional<Folder> findByName(String name);

    /**
     * Find folder by reference
     * @param reference the folder reference to search for
     * @return optional folder with matching reference
     */
    Optional<Folder> findByReference(String reference);

    /**
     * Find folders by archive box
     * @param archiveBox the archive box to filter by
     * @return list of folders in the archive box
     */
    List<Folder> findByArchiveBox(ArchiveBox archiveBox);

    /**
     * Find folders by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of folders containing the name
     */
    @Query("SELECT f FROM Folder f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Folder> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find folders by reference containing (case insensitive)
     * @param reference the partial reference to search for
     * @return list of folders containing the reference
     */
    @Query("SELECT f FROM Folder f WHERE LOWER(f.reference) LIKE LOWER(CONCAT('%', :reference, '%'))")
    List<Folder> findByReferenceContainingIgnoreCase(@Param("reference") String reference);

    /**
     * Find folders by archive box ordered by reference
     * @param archiveBox the archive box to filter by
     * @return list of folders ordered by reference ascending
     */
    List<Folder> findByArchiveBoxOrderByReferenceAsc(ArchiveBox archiveBox);

    /**
     * Find folders by archive box ordered by name
     * @param archiveBox the archive box to filter by
     * @return list of folders ordered by name ascending
     */
    List<Folder> findByArchiveBoxOrderByNameAsc(ArchiveBox archiveBox);

    /**
     * Find all folders ordered by reference
     * @return list of folders ordered by reference ascending
     */
    List<Folder> findAllByOrderByReferenceAsc();

    /**
     * Check if folder exists by name
     * @param name the folder name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if folder exists by reference
     * @param reference the folder reference to check
     * @return true if exists, false otherwise
     */
    boolean existsByReference(String reference);

    /**
     * Find folder by name (case insensitive)
     * @param name the folder name to search for
     * @return optional folder with matching name
     */
    @Query("SELECT f FROM Folder f WHERE LOWER(f.name) = LOWER(:name)")
    Optional<Folder> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find folder by reference (case insensitive)
     * @param reference the folder reference to search for
     * @return optional folder with matching reference
     */
    @Query("SELECT f FROM Folder f WHERE LOWER(f.reference) = LOWER(:reference)")
    Optional<Folder> findByReferenceIgnoreCase(@Param("reference") String reference);

    /**
     * Find all active folders
     * @return list of active folders
     */
    @Query("SELECT f FROM Folder f WHERE f.active = true")
    List<Folder> findAllActive();

    /**
     * Find active folders by archive box
     * @param archiveBox the archive box to filter by
     * @return list of active folders in the archive box
     */
    @Query("SELECT f FROM Folder f WHERE f.archiveBox = :archiveBox AND f.active = true")
    List<Folder> findActiveByArchiveBox(@Param("archiveBox") ArchiveBox archiveBox);

    /**
     * Count folders by archive box
     * @param archiveBox the archive box to count folders for
     * @return count of folders in the archive box
     */
    long countByArchiveBox(ArchiveBox archiveBox);

    /**
     * Count active folders
     * @return number of active folders
     */
    @Query("SELECT COUNT(f) FROM Folder f WHERE f.active = true")
    long countActive();

    /**
     * Search folders by content (name, reference)
     * @param searchTerm the search term to match
     * @return list of folders matching the search term
     */
    @Query("SELECT f FROM Folder f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(f.reference) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Folder> searchByContent(@Param("searchTerm") String searchTerm);
}