package dz.mdn.raas.common.document.repository;

import dz.mdn.raas.common.document.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DocumentType entity operations
 * Manages document type data access and queries
 */
@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    /**
     * Find document type by name
     * @param name the type name to search for
     * @return optional document type with matching name
     */
    Optional<DocumentType> findByName(String name);

    /**
     * Find document types by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of document types containing the name
     */
    @Query("SELECT dt FROM DocumentType dt WHERE LOWER(dt.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<DocumentType> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all document types ordered by name
     * @return list of document types ordered by name ascending
     */
    List<DocumentType> findAllByOrderByNameAsc();

    /**
     * Check if document type exists by name
     * @param name the type name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find document type by name (case insensitive)
     * @param name the type name to search for
     * @return optional document type with matching name
     */
    @Query("SELECT dt FROM DocumentType dt WHERE LOWER(dt.name) = LOWER(:name)")
    Optional<DocumentType> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active document types
     * @return list of active document types
     */
    @Query("SELECT dt FROM DocumentType dt WHERE dt.active = true")
    List<DocumentType> findAllActive();

    /**
     * Count active document types
     * @return number of active document types
     */
    @Query("SELECT COUNT(dt) FROM DocumentType dt WHERE dt.active = true")
    long countActive();
}