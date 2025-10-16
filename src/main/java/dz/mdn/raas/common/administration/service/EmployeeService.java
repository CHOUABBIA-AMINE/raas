/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EmployeeService
 *	@CreatedOn	: 10-16-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.service;

import dz.mdn.raas.common.administration.model.Employee;
import dz.mdn.raas.common.administration.model.Person;
import dz.mdn.raas.common.administration.model.MilitaryRank;
import dz.mdn.raas.common.administration.model.Job;
import dz.mdn.raas.common.administration.repository.EmployeeRepository;
import dz.mdn.raas.common.administration.repository.PersonRepository;
import dz.mdn.raas.common.administration.repository.MilitaryRankRepository;
import dz.mdn.raas.common.administration.repository.JobRepository;
import dz.mdn.raas.common.administration.dto.EmployeeDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * Employee Service with CRUD operations
 * Handles employee management operations with military personnel administration
 * Based on exact field names: F_01=serial, F_02=hiringDate, F_03=person, F_04=militaryRank, F_05=job
 * F_03 (person) is required foreign key
 * F_04 (militaryRank) is required foreign key
 * F_05 (job) is optional foreign key
 * F_01 (serial) and F_02 (hiringDate) are optional
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PersonRepository personRepository;
    private final MilitaryRankRepository militaryRankRepository;
    private final JobRepository jobRepository;

    // ========== CREATE OPERATIONS ==========

    /**
     * Create new employee
     */
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        log.info("Creating employee with serial: {}, Person ID: {}, Rank ID: {}, Job ID: {}, Hiring Date: {}", 
                employeeDTO.getSerial(), employeeDTO.getPersonId(), 
                employeeDTO.getMilitaryRankId(), employeeDTO.getJobId(),
                employeeDTO.getHiringDate());

        // Validate required fields
        validateRequiredFields(employeeDTO, "create");

        // Validate foreign key relationships
        Person person = validateAndGetPerson(employeeDTO.getPersonId());
        MilitaryRank militaryRank = validateAndGetMilitaryRank(employeeDTO.getMilitaryRankId());
        Job job = null;
        if (employeeDTO.getJobId() != null) {
            job = validateAndGetJob(employeeDTO.getJobId());
        }

        // Check for unique constraints
        validateUniqueConstraints(employeeDTO, null);

        // Create entity with exact field mapping
        Employee employee = new Employee();
        employee.setSerial(employeeDTO.getSerial()); // F_01
        employee.setHiringDate(employeeDTO.getHiringDate()); // F_02
        employee.setPerson(person); // F_03
        employee.setMilitaryRank(militaryRank); // F_04
        employee.setJob(job); // F_05

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Successfully created employee with ID: {}", savedEmployee.getId());

        return EmployeeDTO.fromEntity(savedEmployee);
    }

    // ========== READ OPERATIONS ==========

    /**
     * Get employee by ID
     */
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        log.debug("Getting employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        return EmployeeDTO.fromEntity(employee);
    }

    /**
     * Get employee entity by ID
     */
    @Transactional(readOnly = true)
    public Employee getEmployeeEntityById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
    }

    /**
     * Find employee by serial (F_01)
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findBySerial(String serial) {
        log.debug("Finding employee with serial: {}", serial);

        return employeeRepository.findBySerial(serial)
                .map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employee by person ID (F_03)
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findByPersonId(Long personId) {
        log.debug("Finding employee with person ID: {}", personId);

        return employeeRepository.findByPersonId(personId)
                .map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees by hiring date (F_02)
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByHiringDate(Date hiringDate, Pageable pageable) {
        log.debug("Finding employees with hiring date: {}", hiringDate);

        Page<Employee> employees = employeeRepository.findByHiringDate(hiringDate, pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees by military rank ID (F_04)
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByMilitaryRankId(Long militaryRankId, Pageable pageable) {
        log.debug("Finding employees with military rank ID: {}", militaryRankId);

        Page<Employee> employees = employeeRepository.findByMilitaryRankId(militaryRankId, pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees by job ID (F_05)
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByJobId(Long jobId, Pageable pageable) {
        log.debug("Finding employees with job ID: {}", jobId);

        Page<Employee> employees = employeeRepository.findByJobId(jobId, pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Get all employees with pagination
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        log.debug("Getting all employees with pagination");

        Page<Employee> employees = employeeRepository.findAllOrderByPersonName(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Get all employees ordered by rank and name
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAllEmployeesOrderedByRank(Pageable pageable) {
        log.debug("Getting all employees ordered by rank and name");

        Page<Employee> employees = employeeRepository.findAllOrderByRankAndName(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find one employee by ID
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findOne(Long id) {
        log.debug("Finding employee by ID: {}", id);

        return employeeRepository.findById(id)
                .map(EmployeeDTO::fromEntity);
    }

    /**
     * Search employees by name or serial
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> searchEmployees(String searchTerm, Pageable pageable) {
        log.debug("Searching employees with term: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllEmployees(pageable);
        }

        Page<Employee> employees = employeeRepository.searchByPersonNameOrSerial(searchTerm.trim(), pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Search employees with comprehensive context
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> searchEmployeesWithContext(String searchTerm, Pageable pageable) {
        log.debug("Searching employees with context for term: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllEmployees(pageable);
        }

        Page<Employee> employees = employeeRepository.searchWithComprehensiveContext(searchTerm.trim(), pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees by hiring year
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByHiringYear(Integer year, Pageable pageable) {
        log.debug("Finding employees hired in year: {}", year);

        Page<Employee> employees = employeeRepository.findByHiringYear(year, pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees by hiring date range
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByHiringDateRange(Date startDate, Date endDate, Pageable pageable) {
        log.debug("Finding employees hired between {} and {}", startDate, endDate);

        Page<Employee> employees = employeeRepository.findByHiringDateBetween(startDate, endDate, pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees by years of service range
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByYearsOfServiceRange(Integer minYears, Integer maxYears, Pageable pageable) {
        log.debug("Finding employees with {} to {} years of service", minYears, maxYears);

        Page<Employee> employees = employeeRepository.findByYearsOfServiceRange(minYears, maxYears, pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find retirement eligible employees
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findRetirementEligible(Pageable pageable) {
        log.debug("Finding retirement eligible employees");

        Page<Employee> employees = employeeRepository.findRetirementEligible(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find new recruits
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findNewRecruits(Pageable pageable) {
        log.debug("Finding new recruit employees");

        Page<Employee> employees = employeeRepository.findNewRecruits(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find veteran employees
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findVeteranEmployees(Pageable pageable) {
        log.debug("Finding veteran employees");

        Page<Employee> employees = employeeRepository.findVeteranEmployees(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees without job assignment
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findWithoutJobAssignment(Pageable pageable) {
        log.debug("Finding employees without job assignment");

        Page<Employee> employees = employeeRepository.findWithoutJobAssignment(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees with job assignment
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findWithJobAssignment(Pageable pageable) {
        log.debug("Finding employees with job assignment");

        Page<Employee> employees = employeeRepository.findWithJobAssignment(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find officers
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findOfficers(Pageable pageable) {
        log.debug("Finding officer employees");

        Page<Employee> employees = employeeRepository.findOfficers(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find enlisted personnel
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findEnlistedPersonnel(Pageable pageable) {
        log.debug("Finding enlisted personnel");

        Page<Employee> employees = employeeRepository.findEnlistedPersonnel(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find NCOs
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findNCOs(Pageable pageable) {
        log.debug("Finding NCO employees");

        Page<Employee> employees = employeeRepository.findNCOs(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find promotion eligible employees
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findPromotionEligible(Pageable pageable) {
        log.debug("Finding promotion eligible employees");

        Page<Employee> employees = employeeRepository.findPromotionEligible(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees with incomplete profiles
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findWithIncompleteProfiles(Pageable pageable) {
        log.debug("Finding employees with incomplete profiles");

        Page<Employee> employees = employeeRepository.findWithIncompleteProfiles(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees hired this year
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findHiredThisYear(Pageable pageable) {
        log.debug("Finding employees hired this year");

        Page<Employee> employees = employeeRepository.findHiredThisYear(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find service anniversaries this month
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findServiceAnniversariesThisMonth(Pageable pageable) {
        log.debug("Finding service anniversaries this month");

        Page<Employee> employees = employeeRepository.findServiceAnniversariesThisMonth(pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees by military rank designation
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByMilitaryRankDesignation(String rankDesignation, Pageable pageable) {
        log.debug("Finding employees by military rank designation: {}", rankDesignation);

        Page<Employee> employees = employeeRepository.findByMilitaryRankDesignation(rankDesignation, pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    /**
     * Find employees by job designation
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByJobDesignation(String jobDesignation, Pageable pageable) {
        log.debug("Finding employees by job designation: {}", jobDesignation);

        Page<Employee> employees = employeeRepository.findByJobDesignation(jobDesignation, pageable);
        return employees.map(EmployeeDTO::fromEntity);
    }

    // ========== UPDATE OPERATIONS ==========

    /**
     * Update employee
     */
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        log.info("Updating employee with ID: {}", id);

        Employee existingEmployee = getEmployeeEntityById(id);

        // Validate required fields
        validateRequiredFields(employeeDTO, "update");

        // Validate foreign key relationships if being updated
        Person person = null;
        if (employeeDTO.getPersonId() != null) {
            person = validateAndGetPerson(employeeDTO.getPersonId());
        }

        MilitaryRank militaryRank = null;
        if (employeeDTO.getMilitaryRankId() != null) {
            militaryRank = validateAndGetMilitaryRank(employeeDTO.getMilitaryRankId());
        }

        Job job = null;
        if (employeeDTO.getJobId() != null) {
            job = validateAndGetJob(employeeDTO.getJobId());
        }

        // Check for unique constraints (excluding current record)
        validateUniqueConstraints(employeeDTO, id);

        // Update fields with exact field mapping
        existingEmployee.setSerial(employeeDTO.getSerial()); // F_01
        existingEmployee.setHiringDate(employeeDTO.getHiringDate()); // F_02
        if (person != null) {
            existingEmployee.setPerson(person); // F_03
        }
        if (militaryRank != null) {
            existingEmployee.setMilitaryRank(militaryRank); // F_04
        }
        existingEmployee.setJob(job); // F_05 (can be null)

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Successfully updated employee with ID: {}", id);

        return EmployeeDTO.fromEntity(updatedEmployee);
    }

    // ========== DELETE OPERATIONS ==========

    /**
     * Delete employee
     */
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);

        Employee employee = getEmployeeEntityById(id);
        employeeRepository.delete(employee);

        log.info("Successfully deleted employee with ID: {}", id);
    }

    /**
     * Delete employee by ID (direct)
     */
    public void deleteEmployeeById(Long id) {
        log.info("Deleting employee by ID: {}", id);

        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with ID: " + id);
        }

        employeeRepository.deleteById(id);
        log.info("Successfully deleted employee with ID: {}", id);
    }

    // ========== UTILITY METHODS ==========

    /**
     * Check if employee exists
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }

    /**
     * Check if serial exists
     */
    @Transactional(readOnly = true)
    public boolean existsBySerial(String serial) {
        return employeeRepository.existsBySerial(serial);
    }

    /**
     * Get total count of employees
     */
    @Transactional(readOnly = true)
    public Long getTotalCount() {
        return employeeRepository.countAllEmployees();
    }

    /**
     * Get count by military rank
     */
    @Transactional(readOnly = true)
    public Long getCountByMilitaryRank(Long militaryRankId) {
        return employeeRepository.countByMilitaryRankId(militaryRankId);
    }

    /**
     * Get count by job
     */
    @Transactional(readOnly = true)
    public Long getCountByJob(Long jobId) {
        return employeeRepository.countByJobId(jobId);
    }

    /**
     * Get count of new recruits
     */
    @Transactional(readOnly = true)
    public Long getNewRecruitsCount() {
        return employeeRepository.countNewRecruits();
    }

    /**
     * Get count of veteran employees
     */
    @Transactional(readOnly = true)
    public Long getVeteranEmployeesCount() {
        return employeeRepository.countVeteranEmployees();
    }

    /**
     * Get count of retirement eligible
     */
    @Transactional(readOnly = true)
    public Long getRetirementEligibleCount() {
        return employeeRepository.countRetirementEligible();
    }

    /**
     * Get count without job assignment
     */
    @Transactional(readOnly = true)
    public Long getCountWithoutJobAssignment() {
        return employeeRepository.countWithoutJobAssignment();
    }

    // ========== VALIDATION METHODS ==========

    /**
     * Validate required fields
     */
    private void validateRequiredFields(EmployeeDTO employeeDTO, String operation) {
        if (employeeDTO.getPersonId() == null) {
            throw new RuntimeException("Person is required for " + operation);
        }
        if (employeeDTO.getMilitaryRankId() == null) {
            throw new RuntimeException("Military rank is required for " + operation);
        }
    }

    /**
     * Validate unique constraints
     */
    private void validateUniqueConstraints(EmployeeDTO employeeDTO, Long excludeId) {
        // Check serial uniqueness if provided
        if (employeeDTO.getSerial() != null && !employeeDTO.getSerial().trim().isEmpty()) {
            if (excludeId == null) {
                if (employeeRepository.existsBySerial(employeeDTO.getSerial())) {
                    throw new RuntimeException("Employee with serial '" + employeeDTO.getSerial() + "' already exists");
                }
            } else {
                if (employeeRepository.existsBySerialAndIdNot(employeeDTO.getSerial(), excludeId)) {
                    throw new RuntimeException("Another employee with serial '" + employeeDTO.getSerial() + "' already exists");
                }
            }
        }

        // Check person uniqueness - one person can only have one employee record
        if (excludeId == null) {
            if (employeeRepository.findByPersonId(employeeDTO.getPersonId()).isPresent()) {
                throw new RuntimeException("Person with ID " + employeeDTO.getPersonId() + " already has an employee record");
            }
        } else {
            Optional<Employee> existingEmployee = employeeRepository.findByPersonId(employeeDTO.getPersonId());
            if (existingEmployee.isPresent() && !existingEmployee.get().getId().equals(excludeId)) {
                throw new RuntimeException("Another employee record exists for person ID " + employeeDTO.getPersonId());
            }
        }
    }

    /**
     * Validate and get person
     */
    private Person validateAndGetPerson(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Person not found with ID: " + personId));
    }

    /**
     * Validate and get military rank
     */
    private MilitaryRank validateAndGetMilitaryRank(Long militaryRankId) {
        return militaryRankRepository.findById(militaryRankId)
                .orElseThrow(() -> new RuntimeException("Military rank not found with ID: " + militaryRankId));
    }

    /**
     * Validate and get job
     */
    private Job validateAndGetJob(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
    }
}
