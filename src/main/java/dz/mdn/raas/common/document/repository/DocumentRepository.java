package dz.mdn.raas.common.document.repository;

import dz.mdn.raas.common.document.model.Document;
import dz.mdn.raas.common.document.model.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Document entity operations
 * Manages document data access and queries
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * Find document by reference number
     * @param reference the document reference to search for
     * @return optional document with matching reference
     */
    Optional<Document> findByReference(String reference);

    /**
     * Find documents by document type
     * @param documentType the document type to filter by
     * @return list of documents with matching type
     */
    List<Document> findByDocumentType(DocumentType documentType);

    /**
     * Find documents by title containing (case insensitive)
     * @param title the partial title to search for
     * @return list of documents containing the title
     */
    @Query("SELECT d FROM Document d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Document> findByTitleContainingIgnoreCase(@Param("title") String title);

    /**
     * Find documents by creation date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of documents within the creation date range
     */
    @Query("SELECT d FROM Document d WHERE d.creationDate BETWEEN :startDate AND :endDate")
    List<Document> findByCreationDateBetween(@Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);

    /**
     * Find documents created today
     * @return list of documents created today
     */
    @Query("SELECT d FROM Document d WHERE DATE(d.creationDate) = CURRENT_DATE")
    List<Document> findCreatedToday();

    /**
     * Find documents created after specified date
     * @param date the date to compare against
     * @return list of documents created after the date
     */
    List<Document> findByCreationDateAfter(LocalDateTime date);

    /**
     * Find documents ordered by creation date desc
     * @param pageable pagination information
     * @return paginated list of documents ordered by creation date descending
     */
    Page<Document> findAllByOrderByCreationDateDesc(Pageable pageable);

    /**
     * Find documents by type ordered by creation date desc
     * @param documentType the document type to filter by
     * @return list of documents ordered by creation date descending
     */
    List<Document> findByDocumentTypeOrderByCreationDateDesc(DocumentType documentType);

    /**
     * Count documents by type
     * @param documentType the document type to count
     * @return count of documents with the type
     */
    long countByDocumentType(DocumentType documentType);

    /**
     * Check if document exists by reference
     * @param reference the document reference to check
     * @return true if exists, false otherwise
     */
    boolean existsByReference(String reference);

    /**
     * Find documents created in current month
     * @return list of documents created this month
     */
    @Query("SELECT d FROM Document d WHERE YEAR(d.creationDate) = YEAR(CURRENT_DATE) AND MONTH(d.creationDate) = MONTH(CURRENT_DATE)")
    List<Document> findCreatedThisMonth();

    /**
     * Find documents created in current year
     * @return list of documents created this year
     */
    @Query("SELECT d FROM Document d WHERE YEAR(d.creationDate) = YEAR(CURRENT_DATE)")
    List<Document> findCreatedThisYear();

    /**
     * Find documents by reference containing (case insensitive)
     * @param reference the partial reference to search for
     * @return list of documents containing the reference
     */
    @Query("SELECT d FROM Document d WHERE LOWER(d.reference) LIKE LOWER(CONCAT('%', :reference, '%'))")
    List<Document> findByReferenceContainingIgnoreCase(@Param("reference") String reference);

    /**
     * Search documents by content (title, reference)
     * @param searchTerm the search term to match
     * @return list of documents matching the search term
     */
    @Query("SELECT d FROM Document d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.reference) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Document> searchByContent(@Param("searchTerm") String searchTerm);

    /**
     * Find latest documents by type
     * @param documentType the document type to filter by
     * @param limit maximum number of documents to return
     * @return list of latest documents by type
     */
    @Query(value = "SELECT d FROM Document d WHERE d.documentType = :documentType ORDER BY d.creationDate DESC")
    List<Document> findLatestByDocumentType(@Param("documentType") DocumentType documentType, Pageable pageable);
}