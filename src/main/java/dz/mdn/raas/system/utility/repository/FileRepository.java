package dz.mdn.raas.system.utility.repository;

import dz.mdn.raas.system.utility.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for File entity operations
 * Provides CRUD operations and custom queries for file management
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * Find files by original filename
     * @param originalFilename the original filename to search for
     * @return list of files with matching original filename
     */
    List<File> findByOriginalFilename(String originalFilename);

    /**
     * Find files by content type
     * @param contentType the MIME type to search for
     * @return list of files with matching content type
     */
    List<File> findByContentType(String contentType);

    /**
     * Find file by unique filename
     * @param filename the unique filename to search for
     * @return optional file with matching filename
     */
    Optional<File> findByFilename(String filename);

    /**
     * Find files by size range
     * @param minSize minimum file size
     * @param maxSize maximum file size
     * @return list of files within the size range
     */
    @Query("SELECT f FROM File f WHERE f.size BETWEEN :minSize AND :maxSize")
    List<File> findByFileSizeRange(@Param("minSize") Long minSize, @Param("maxSize") Long maxSize);

    /**
     * Find files larger than specified size
     * @param size the minimum size threshold
     * @return list of files larger than the specified size
     */
    List<File> findBySizeGreaterThan(Long size);

    /**
     * Count files by content type
     * @param contentType the content type to count
     * @return number of files with the specified content type
     */
    long countByContentType(String contentType);

    /**
     * Check if file exists by filename
     * @param filename the filename to check
     * @return true if file exists, false otherwise
     */
    boolean existsByFilename(String filename);

    /**
     * Delete file by filename
     * @param filename the filename to delete
     */
    void deleteByFilename(String filename);
}