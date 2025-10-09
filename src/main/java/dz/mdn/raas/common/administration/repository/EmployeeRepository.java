package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.Employee;
import dz.mdn.raas.common.administration.model.Job;
import dz.mdn.raas.common.administration.model.MilitaryRank;
import dz.mdn.raas.common.administration.model.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Employee entity operations
 * Manages employee data access and queries
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find employee by registration number
     * @param registrationNumber the registration number to search for
     * @return optional employee with matching registration number
     */
    Optional<Employee> findByRegistrationNumber(String registrationNumber);

    /**
     * Find employees by structure
     * @param structure the structure to filter by
     * @return list of employees in the structure
     */
    List<Employee> findByStructure(Structure structure);

    /**
     * Find employees by job
     * @param job the job to filter by
     * @return list of employees with the job
     */
    List<Employee> findByJob(Job job);

    /**
     * Find employees by military rank
     * @param militaryRank the military rank to filter by
     * @return list of employees with the military rank
     */
    List<Employee> findByMilitaryRank(MilitaryRank militaryRank);

    /**
     * Find employees by structure and job
     * @param structure the structure to filter by
     * @param job the job to filter by
     * @return list of employees matching both criteria
     */
    List<Employee> findByStructureAndJob(Structure structure, Job job);

    /**
     * Find employees by structure ordered by registration number
     * @param structure the structure to filter by
     * @return list of employees ordered by registration number ascending
     */
    List<Employee> findByStructureOrderByRegistrationNumberAsc(Structure structure);

    /**
     * Find all employees ordered by registration number
     * @return list of employees ordered by registration number ascending
     */
    List<Employee> findAllByOrderByRegistrationNumberAsc();

    /**
     * Check if employee exists by registration number
     * @param registrationNumber the registration number to check
     * @return true if exists, false otherwise
     */
    boolean existsByRegistrationNumber(String registrationNumber);

    /**
     * Find employee by registration number (case insensitive)
     * @param registrationNumber the registration number to search for
     * @return optional employee with matching registration number
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.registrationNumber) = LOWER(:registrationNumber)")
    Optional<Employee> findByRegistrationNumberIgnoreCase(@Param("registrationNumber") String registrationNumber);

    /**
     * Find all active employees
     * @return list of active employees
     */
    @Query("SELECT e FROM Employee e WHERE e.active = true")
    List<Employee> findAllActive();

    /**
     * Find active employees by structure
     * @param structure the structure to filter by
     * @return list of active employees in the structure
     */
    @Query("SELECT e FROM Employee e WHERE e.structure = :structure AND e.active = true")
    List<Employee> findActiveByStructure(@Param("structure") Structure structure);

    /**
     * Count employees by structure
     * @param structure the structure to count employees for
     * @return count of employees in the structure
     */
    long countByStructure(Structure structure);

    /**
     * Count employees by job
     * @param job the job to count employees for
     * @return count of employees with the job
     */
    long countByJob(Job job);

    /**
     * Count active employees
     * @return number of active employees
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.active = true")
    long countActive();

    /**
     * Count active employees by structure
     * @param structure the structure to count active employees for
     * @return count of active employees in the structure
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.structure = :structure AND e.active = true")
    long countActiveByStructure(@Param("structure") Structure structure);
}