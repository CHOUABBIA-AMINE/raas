/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EmployeeRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * Employee Repository with essential CRUD operations
 * Based on exact field names: F_00=id, F_01=serial, F_02=hiringDate, F_03=person, F_04=militaryRank, F_05=job
 * F_03 (person) is required foreign key
 * F_04 (militaryRank) is required foreign key
 * F_05 (job) is optional foreign key
 * F_01 (serial) and F_02 (hiringDate) are optional
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find employee by serial (F_01)
     */
    @Query("SELECT e FROM Employee e WHERE e.serial = :serial")
    Optional<Employee> findBySerial(@Param("serial") String serial);

    /**
     * Find employees by hiring date (F_02)
     */
    @Query("SELECT e FROM Employee e WHERE e.hiringDate = :hiringDate")
    Page<Employee> findByHiringDate(@Param("hiringDate") Date hiringDate, Pageable pageable);

    /**
     * Find employee by person ID (F_03)
     */
    @Query("SELECT e FROM Employee e WHERE e.person.id = :personId")
    Optional<Employee> findByPersonId(@Param("personId") Long personId);

    /**
     * Find employees by military rank ID (F_04)
     */
    @Query("SELECT e FROM Employee e WHERE e.militaryRank.id = :militaryRankId ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findByMilitaryRankId(@Param("militaryRankId") Long militaryRankId, Pageable pageable);

    /**
     * Find employees by job ID (F_05)
     */
    @Query("SELECT e FROM Employee e WHERE e.job.id = :jobId ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findByJobId(@Param("jobId") Long jobId, Pageable pageable);

    /**
     * Find all employees with pagination ordered by person name
     */
    @Query("SELECT e FROM Employee e ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC, e.person.firstnameAr ASC, e.person.lastnameAr ASC")
    Page<Employee> findAllOrderByPersonName(Pageable pageable);

    /**
     * Find all employees ordered by military rank level then name
     */
    @Query("SELECT e FROM Employee e ORDER BY e.militaryRank.rankLevel DESC, e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findAllOrderByRankAndName(Pageable pageable);

    /**
     * Search employees by person name or serial
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "e.person.firstnameAr LIKE %:search% OR " +
           "e.person.lastnameAr LIKE %:search% OR " +
           "e.person.firstnameLt LIKE %:search% OR " +
           "e.person.lastnameLt LIKE %:search% OR " +
           "e.serial LIKE %:search%")
    Page<Employee> searchByPersonNameOrSerial(@Param("search") String search, Pageable pageable);

    /**
     * Find employees by hiring year
     */
    @Query("SELECT e FROM Employee e WHERE YEAR(e.hiringDate) = :year")
    Page<Employee> findByHiringYear(@Param("year") Integer year, Pageable pageable);

    /**
     * Find employees by hiring date range
     */
    @Query("SELECT e FROM Employee e WHERE e.hiringDate BETWEEN :startDate AND :endDate")
    Page<Employee> findByHiringDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    /**
     * Find employees by years of service range
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(e.hiringDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.hiringDate) OR (MONTH(CURRENT_DATE) = MONTH(e.hiringDate) AND DAY(CURRENT_DATE) < DAY(e.hiringDate)) THEN 1 ELSE 0 END " +
           "BETWEEN :minYears AND :maxYears")
    Page<Employee> findByYearsOfServiceRange(@Param("minYears") Integer minYears, @Param("maxYears") Integer maxYears, Pageable pageable);

    /**
     * Find employees eligible for retirement (30+ years or age 60+)
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "(" +
           "YEAR(CURRENT_DATE) - YEAR(e.hiringDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.hiringDate) OR (MONTH(CURRENT_DATE) = MONTH(e.hiringDate) AND DAY(CURRENT_DATE) < DAY(e.hiringDate)) THEN 1 ELSE 0 END >= 30" +
           ") OR (" +
           "YEAR(CURRENT_DATE) - YEAR(e.person.birthDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.person.birthDate) OR (MONTH(CURRENT_DATE) = MONTH(e.person.birthDate) AND DAY(CURRENT_DATE) < DAY(e.person.birthDate)) THEN 1 ELSE 0 END >= 60" +
           ")")
    Page<Employee> findRetirementEligible(Pageable pageable);

    /**
     * Find new recruits (less than 2 years service)
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(e.hiringDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.hiringDate) OR (MONTH(CURRENT_DATE) = MONTH(e.hiringDate) AND DAY(CURRENT_DATE) < DAY(e.hiringDate)) THEN 1 ELSE 0 END < 2")
    Page<Employee> findNewRecruits(Pageable pageable);

    /**
     * Find veteran employees (20+ years service)
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(e.hiringDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.hiringDate) OR (MONTH(CURRENT_DATE) = MONTH(e.hiringDate) AND DAY(CURRENT_DATE) < DAY(e.hiringDate)) THEN 1 ELSE 0 END >= 20")
    Page<Employee> findVeteranEmployees(Pageable pageable);

    /**
     * Find employees without job assignment
     */
    @Query("SELECT e FROM Employee e WHERE e.job IS NULL ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findWithoutJobAssignment(Pageable pageable);

    /**
     * Find employees with job assignment
     */
    @Query("SELECT e FROM Employee e WHERE e.job IS NOT NULL ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findWithJobAssignment(Pageable pageable);

    /**
     * Find employees without serial
     */
    @Query("SELECT e FROM Employee e WHERE e.serial IS NULL OR e.serial = '' ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findWithoutSerial(Pageable pageable);

    /**
     * Find employees without hiring date
     */
    @Query("SELECT e FROM Employee e WHERE e.hiringDate IS NULL ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findWithoutHiringDate(Pageable pageable);

    /**
     * Count total employees
     */
    @Query("SELECT COUNT(e) FROM Employee e")
    Long countAllEmployees();

    /**
     * Count employees by military rank
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.militaryRank.id = :militaryRankId")
    Long countByMilitaryRankId(@Param("militaryRankId") Long militaryRankId);

    /**
     * Count employees by job
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.job.id = :jobId")
    Long countByJobId(@Param("jobId") Long jobId);

    /**
     * Count employees by service years range
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(e.hiringDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.hiringDate) OR (MONTH(CURRENT_DATE) = MONTH(e.hiringDate) AND DAY(CURRENT_DATE) < DAY(e.hiringDate)) THEN 1 ELSE 0 END " +
           "BETWEEN :minYears AND :maxYears")
    Long countByYearsOfServiceRange(@Param("minYears") Integer minYears, @Param("maxYears") Integer maxYears);

    /**
     * Count new recruits
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(e.hiringDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.hiringDate) OR (MONTH(CURRENT_DATE) = MONTH(e.hiringDate) AND DAY(CURRENT_DATE) < DAY(e.hiringDate)) THEN 1 ELSE 0 END < 2")
    Long countNewRecruits();

    /**
     * Count veteran employees
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(e.hiringDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.hiringDate) OR (MONTH(CURRENT_DATE) = MONTH(e.hiringDate) AND DAY(CURRENT_DATE) < DAY(e.hiringDate)) THEN 1 ELSE 0 END >= 20")
    Long countVeteranEmployees();

    /**
     * Count retirement eligible employees
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE " +
           "(" +
           "YEAR(CURRENT_DATE) - YEAR(e.hiringDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.hiringDate) OR (MONTH(CURRENT_DATE) = MONTH(e.hiringDate) AND DAY(CURRENT_DATE) < DAY(e.hiringDate)) THEN 1 ELSE 0 END >= 30" +
           ") OR (" +
           "YEAR(CURRENT_DATE) - YEAR(e.person.birthDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.person.birthDate) OR (MONTH(CURRENT_DATE) = MONTH(e.person.birthDate) AND DAY(CURRENT_DATE) < DAY(e.person.birthDate)) THEN 1 ELSE 0 END >= 60" +
           ")")
    Long countRetirementEligible();

    /**
     * Count employees without job assignment
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.job IS NULL")
    Long countWithoutJobAssignment();

    /**
     * Find employees with join fetch for person, rank and job
     */
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.person LEFT JOIN FETCH e.militaryRank LEFT JOIN FETCH e.job ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findAllWithPersonRankAndJob(Pageable pageable);

    /**
     * Find employees by military rank designation
     */
    @Query("SELECT e FROM Employee e WHERE e.militaryRank.designationFr = :rankDesignation ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findByMilitaryRankDesignation(@Param("rankDesignation") String rankDesignation, Pageable pageable);

    /**
     * Find employees by military rank category
     */
    @Query("SELECT e FROM Employee e WHERE e.militaryRank.militaryCategory = :rankCategory ORDER BY e.militaryRank.rankLevel DESC, e.person.firstnameLt ASC")
    Page<Employee> findByMilitaryRankCategory(@Param("rankCategory") String rankCategory, Pageable pageable);

    /**
     * Find employees by job designation
     */
    @Query("SELECT e FROM Employee e WHERE e.job.designationFr = :jobDesignation ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findByJobDesignation(@Param("jobDesignation") String jobDesignation, Pageable pageable);

    /**
     * Find employees by job structure
     */
    @Query("SELECT e FROM Employee e WHERE e.job.structure.designationFr = :structureDesignation ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findByJobStructureDesignation(@Param("structureDesignation") String structureDesignation, Pageable pageable);

    /**
     * Search employees with comprehensive context
     */
    @Query("SELECT e FROM Employee e LEFT JOIN e.person p LEFT JOIN e.militaryRank mr LEFT JOIN e.job j LEFT JOIN j.structure s WHERE " +
           "(p.firstnameAr LIKE %:search% OR p.lastnameAr LIKE %:search% OR " +
           "p.firstnameLt LIKE %:search% OR p.lastnameLt LIKE %:search% OR " +
           "e.serial LIKE %:search% OR " +
           "mr.designationFr LIKE %:search% OR mr.abbreviationFr LIKE %:search% OR " +
           "j.designationFr LIKE %:search% OR " +
           "s.designationFr LIKE %:search% OR s.acronymFr LIKE %:search%) " +
           "ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> searchWithComprehensiveContext(@Param("search") String search, Pageable pageable);

    /**
     * Find officers (rank category = 'OFFICER' or 'SENIOR_OFFICER')
     */
    @Query("SELECT e FROM Employee e WHERE e.militaryRank.militaryCategory IN ('OFFICER', 'SENIOR_OFFICER') ORDER BY e.militaryRank.rankLevel DESC, e.person.firstnameLt ASC")
    Page<Employee> findOfficers(Pageable pageable);

    /**
     * Find enlisted personnel (rank category = 'ENLISTED')
     */
    @Query("SELECT e FROM Employee e WHERE e.militaryRank.militaryCategory = 'ENLISTED' ORDER BY e.militaryRank.rankLevel DESC, e.person.firstnameLt ASC")
    Page<Employee> findEnlistedPersonnel(Pageable pageable);

    /**
     * Find NCOs (rank category = 'NCO')
     */
    @Query("SELECT e FROM Employee e WHERE e.militaryRank.militaryCategory = 'NCO' ORDER BY e.militaryRank.rankLevel DESC, e.person.firstnameLt ASC")
    Page<Employee> findNCOs(Pageable pageable);

    /**
     * Find employees by serial pattern
     */
    @Query("SELECT e FROM Employee e WHERE e.serial LIKE %:pattern%")
    Page<Employee> findBySerialContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Check if serial exists
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Employee e WHERE e.serial = :serial")
    boolean existsBySerial(@Param("serial") String serial);

    /**
     * Check if serial exists excluding current employee
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Employee e WHERE e.serial = :serial AND e.id != :id")
    boolean existsBySerialAndIdNot(@Param("serial") String serial, @Param("id") Long id);

    /**
     * Find employees hired in current year
     */
    @Query("SELECT e FROM Employee e WHERE YEAR(e.hiringDate) = YEAR(CURRENT_DATE)")
    Page<Employee> findHiredThisYear(Pageable pageable);

    /**
     * Find employees with service anniversaries this month
     */
    @Query("SELECT e FROM Employee e WHERE MONTH(e.hiringDate) = MONTH(CURRENT_DATE)")
    Page<Employee> findServiceAnniversariesThisMonth(Pageable pageable);

    /**
     * Find employees by birth state (from person)
     */
    @Query("SELECT e FROM Employee e WHERE e.person.birthState.id = :stateId ORDER BY e.person.firstnameLt ASC, e.person.lastnameLt ASC")
    Page<Employee> findByPersonBirthStateId(@Param("stateId") Long stateId, Pageable pageable);

    /**
     * Find employees by age range (from person)
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(e.person.birthDate) - CASE WHEN MONTH(CURRENT_DATE) < MONTH(e.person.birthDate) OR (MONTH(CURRENT_DATE) = MONTH(e.person.birthDate) AND DAY(CURRENT_DATE) < DAY(e.person.birthDate)) THEN 1 ELSE 0 END " +
           "BETWEEN :minAge AND :maxAge")
    Page<Employee> findByPersonAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge, Pageable pageable);

    /**
     * Find employees with incomplete profiles
     */
    @Query("SELECT e FROM Employee e WHERE e.serial IS NULL OR e.serial = '' OR e.hiringDate IS NULL OR e.job IS NULL")
    Page<Employee> findWithIncompleteProfiles(Pageable pageable);

    /**
     * Find promotion eligible employees
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "(" +
           "(e.militaryRank.militaryCategory = 'ENLISTED' AND YEAR(CURRENT_DATE) - YEAR(e.hiringDate) >= 2) OR " +
           "(e.militaryRank.militaryCategory = 'NCO' AND YEAR(CURRENT_DATE) - YEAR(e.hiringDate) >= 4) OR " +
           "(e.militaryRank.militaryCategory = 'OFFICER' AND YEAR(CURRENT_DATE) - YEAR(e.hiringDate) >= 3) OR " +
           "(e.militaryRank.militaryCategory = 'SENIOR_OFFICER' AND YEAR(CURRENT_DATE) - YEAR(e.hiringDate) >= 5)" +
           ") " +
           "ORDER BY e.militaryRank.rankLevel ASC, e.hiringDate ASC")
    Page<Employee> findPromotionEligible(Pageable pageable);
}
