package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Job entity operations
 * Manages job/position data access and queries
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    /**
     * Find job by name
     * @param name the job name to search for
     * @return optional job with matching name
     */
    Optional<Job> findByName(String name);

    /**
     * Find jobs by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of jobs containing the name
     */
    @Query("SELECT j FROM Job j WHERE LOWER(j.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Job> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all jobs ordered by name
     * @return list of jobs ordered by name ascending
     */
    List<Job> findAllByOrderByNameAsc();

    /**
     * Check if job exists by name
     * @param name the job name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find job by name (case insensitive)
     * @param name the job name to search for
     * @return optional job with matching name
     */
    @Query("SELECT j FROM Job j WHERE LOWER(j.name) = LOWER(:name)")
    Optional<Job> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active jobs
     * @return list of active jobs
     */
    @Query("SELECT j FROM Job j WHERE j.active = true")
    List<Job> findAllActive();

    /**
     * Count active jobs
     * @return number of active jobs
     */
    @Query("SELECT COUNT(j) FROM Job j WHERE j.active = true")
    long countActive();
}