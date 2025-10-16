/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EmployeeController
 *	@CreatedOn	: 10-16-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.controller;

import dz.mdn.raas.common.administration.service.EmployeeService;
import dz.mdn.raas.common.administration.dto.EmployeeDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Employee REST Controller
 * Handles employee operations: create, get metadata, delete, get all
 * Based on exact Employee model: F_00=id, F_01=serial, F_02=hiringDate, F_03=person, F_04=militaryRank, F_05=job
 * F_03 (person) is required foreign key
 * F_04 (militaryRank) is required foreign key
 * F_05 (job) is optional foreign key
 * F_01 (serial) and F_02 (hiringDate) are optional
 */
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    // ========== POST ONE EMPLOYEE ==========

    /**
     * Create new employee
     * Creates military employee with person assignment, rank designation, and optional job assignment
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("Creating employee with serial: {}, Person ID: {}, Rank ID: {}, Job ID: {}, Hiring Date: {}", 
                employeeDTO.getSerial(), employeeDTO.getPersonId(), 
                employeeDTO.getMilitaryRankId(), employeeDTO.getJobId(),
                employeeDTO.getHiringDate());
        
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    // ========== GET METADATA ==========

    /**
     * Get employee metadata by ID
     * Returns employee information with person details, military rank, job assignment, and service record
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeMetadata(@PathVariable Long id) {
        log.debug("Getting metadata for employee ID: {}", id);
        
        EmployeeDTO employeeMetadata = employeeService.getEmployeeById(id);
        
        return ResponseEntity.ok(employeeMetadata);
    }

    /**
     * Get employee by serial (F_01)
     */
    @GetMapping("/serial/{serial}")
    public ResponseEntity<EmployeeDTO> getEmployeeBySerial(@PathVariable String serial) {
        log.debug("Getting employee by serial: {}", serial);
        
        return employeeService.findBySerial(serial)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get employee by person ID (F_03)
     */
    @GetMapping("/person/{personId}")
    public ResponseEntity<EmployeeDTO> getEmployeeByPersonId(@PathVariable Long personId) {
        log.debug("Getting employee by person ID: {}", personId);
        
        return employeeService.findByPersonId(personId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get employees by hiring date (F_02)
     */
    @GetMapping("/hiring-date/{hiringDate}")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByHiringDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date hiringDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees by hiring date: {}", hiringDate);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "person.firstnameLt"));
        Page<EmployeeDTO> employees = employeeService.findByHiringDate(hiringDate, pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employees by military rank ID (F_04)
     */
    @GetMapping("/military-rank/{militaryRankId}")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByMilitaryRank(
            @PathVariable Long militaryRankId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees by military rank ID: {}", militaryRankId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "person.firstnameLt"));
        Page<EmployeeDTO> employees = employeeService.findByMilitaryRankId(militaryRankId, pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employees by job ID (F_05)
     */
    @GetMapping("/job/{jobId}")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees by job ID: {}", jobId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "person.firstnameLt"));
        Page<EmployeeDTO> employees = employeeService.findByJobId(jobId, pageable);
        
        return ResponseEntity.ok(employees);
    }

    // ========== DELETE ONE ==========

    /**
     * Delete employee by ID
     * Removes employee from the military personnel system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("Deleting employee with ID: {}", id);
        
        employeeService.deleteEmployee(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== GET ALL ==========

    /**
     * Get all employees with pagination
     * Returns list of all military personnel ordered by person name
     */
    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "person.firstnameLt,person.lastnameLt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Getting all employees - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                  page, size, sortBy, sortDir);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        String[] sortFields = sortBy.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortFields));
        
        Page<EmployeeDTO> employees = employeeService.getAllEmployees(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get all employees ordered by military rank and name
     */
    @GetMapping("/ordered-by-rank")
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployeesOrderedByRank(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting all employees ordered by rank and name");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeDTO> employees = employeeService.getAllEmployeesOrderedByRank(pageable);
        
        return ResponseEntity.ok(employees);
    }

    // ========== SEARCH ENDPOINTS ==========

    /**
     * Search employees by name or serial
     */
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeDTO>> searchEmployees(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "person.firstnameLt,person.lastnameLt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Searching employees with query: {}", query);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        String[] sortFields = sortBy.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortFields));
        
        Page<EmployeeDTO> employees = employeeService.searchEmployees(query, pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Search employees with comprehensive context
     */
    @GetMapping("/search/context")
    public ResponseEntity<Page<EmployeeDTO>> searchEmployeesWithContext(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Searching employees with context for query: {}", query);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "person.firstnameLt"));
        Page<EmployeeDTO> employees = employeeService.searchEmployeesWithContext(query, pageable);
        
        return ResponseEntity.ok(employees);
    }

    // ========== SERVICE-BASED ENDPOINTS ==========

    /**
     * Get employees by hiring year
     */
    @GetMapping("/hiring-year/{year}")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByHiringYear(
            @PathVariable Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees hired in year: {}", year);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "hiringDate"));
        Page<EmployeeDTO> employees = employeeService.findByHiringYear(year, pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employees by hiring date range
     */
    @GetMapping("/hiring-date-range")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByHiringDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees hired between {} and {}", startDate, endDate);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "hiringDate"));
        Page<EmployeeDTO> employees = employeeService.findByHiringDateRange(startDate, endDate, pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employees by years of service range
     */
    @GetMapping("/years-of-service")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByYearsOfService(
            @RequestParam Integer minYears,
            @RequestParam Integer maxYears,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees with {} to {} years of service", minYears, maxYears);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "hiringDate"));
        Page<EmployeeDTO> employees = employeeService.findByYearsOfServiceRange(minYears, maxYears, pageable);
        
        return ResponseEntity.ok(employees);
    }

    // ========== MILITARY CLASSIFICATION ENDPOINTS ==========

    /**
     * Get retirement eligible employees
     */
    @GetMapping("/retirement-eligible")
    public ResponseEntity<Page<EmployeeDTO>> getRetirementEligibleEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting retirement eligible employees");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "hiringDate"));
        Page<EmployeeDTO> employees = employeeService.findRetirementEligible(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get new recruit employees
     */
    @GetMapping("/new-recruits")
    public ResponseEntity<Page<EmployeeDTO>> getNewRecruitEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting new recruit employees");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "hiringDate"));
        Page<EmployeeDTO> employees = employeeService.findNewRecruits(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get veteran employees
     */
    @GetMapping("/veterans")
    public ResponseEntity<Page<EmployeeDTO>> getVeteranEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting veteran employees");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "hiringDate"));
        Page<EmployeeDTO> employees = employeeService.findVeteranEmployees(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get officers
     */
    @GetMapping("/officers")
    public ResponseEntity<Page<EmployeeDTO>> getOfficerEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting officer employees");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "militaryRank.rankLevel"));
        Page<EmployeeDTO> employees = employeeService.findOfficers(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get enlisted personnel
     */
    @GetMapping("/enlisted")
    public ResponseEntity<Page<EmployeeDTO>> getEnlistedPersonnel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting enlisted personnel");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "militaryRank.rankLevel"));
        Page<EmployeeDTO> employees = employeeService.findEnlistedPersonnel(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get NCOs
     */
    @GetMapping("/ncos")
    public ResponseEntity<Page<EmployeeDTO>> getNCOEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting NCO employees");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "militaryRank.rankLevel"));
        Page<EmployeeDTO> employees = employeeService.findNCOs(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get promotion eligible employees
     */
    @GetMapping("/promotion-eligible")
    public ResponseEntity<Page<EmployeeDTO>> getPromotionEligibleEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting promotion eligible employees");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "hiringDate"));
        Page<EmployeeDTO> employees = employeeService.findPromotionEligible(pageable);
        
        return ResponseEntity.ok(employees);
    }

    // ========== JOB ASSIGNMENT ENDPOINTS ==========

    /**
     * Get employees without job assignment
     */
    @GetMapping("/without-job")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesWithoutJobAssignment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees without job assignment");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "person.firstnameLt"));
        Page<EmployeeDTO> employees = employeeService.findWithoutJobAssignment(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employees with job assignment
     */
    @GetMapping("/with-job")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesWithJobAssignment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees with job assignment");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "person.firstnameLt"));
        Page<EmployeeDTO> employees = employeeService.findWithJobAssignment(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employees with incomplete profiles
     */
    @GetMapping("/incomplete-profiles")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesWithIncompleteProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees with incomplete profiles");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "person.firstnameLt"));
        Page<EmployeeDTO> employees = employeeService.findWithIncompleteProfiles(pageable);
        
        return ResponseEntity.ok(employees);
    }

    // ========== TEMPORAL ENDPOINTS ==========

    /**
     * Get employees hired this year
     */
    @GetMapping("/hired-this-year")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesHiredThisYear(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees hired this year");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "hiringDate"));
        Page<EmployeeDTO> employees = employeeService.findHiredThisYear(pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get service anniversaries this month
     */
    @GetMapping("/anniversaries-this-month")
    public ResponseEntity<Page<EmployeeDTO>> getServiceAnniversariesThisMonth(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting service anniversaries this month");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "hiringDate"));
        Page<EmployeeDTO> employees = employeeService.findServiceAnniversariesThisMonth(pageable);
        
        return ResponseEntity.ok(employees);
    }

    // ========== DESIGNATION-BASED ENDPOINTS ==========

    /**
     * Get employees by military rank designation
     */
    @GetMapping("/rank-designation/{rankDesignation}")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByMilitaryRankDesignation(
            @PathVariable String rankDesignation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees by military rank designation: {}", rankDesignation);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "person.firstnameLt"));
        Page<EmployeeDTO> employees = employeeService.findByMilitaryRankDesignation(rankDesignation, pageable);
        
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employees by job designation
     */
    @GetMapping("/job-designation/{jobDesignation}")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByJobDesignation(
            @PathVariable String jobDesignation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting employees by job designation: {}", jobDesignation);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "person.firstnameLt"));
        Page<EmployeeDTO> employees = employeeService.findByJobDesignation(jobDesignation, pageable);
        
        return ResponseEntity.ok(employees);
    }

    // ========== UPDATE ENDPOINTS ==========

    /**
     * Update employee metadata
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        
        log.info("Updating employee with ID: {}", id);
        
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        
        return ResponseEntity.ok(updatedEmployee);
    }

    // ========== VALIDATION ENDPOINTS ==========

    /**
     * Check if employee exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkEmployeeExists(@PathVariable Long id) {
        log.debug("Checking existence of employee ID: {}", id);
        
        boolean exists = employeeService.existsById(id);
        
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if serial exists
     */
    @GetMapping("/exists/serial/{serial}")
    public ResponseEntity<Boolean> checkEmployeeExistsBySerial(@PathVariable String serial) {
        log.debug("Checking existence by serial: {}", serial);
        
        boolean exists = employeeService.existsBySerial(serial);
        
        return ResponseEntity.ok(exists);
    }

    // ========== STATISTICS ENDPOINTS ==========

    /**
     * Get total count of employees
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getEmployeesCount() {
        log.debug("Getting total count of employees");
        
        Long count = employeeService.getTotalCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count by military rank
     */
    @GetMapping("/count/military-rank/{militaryRankId}")
    public ResponseEntity<Long> getCountByMilitaryRank(@PathVariable Long militaryRankId) {
        log.debug("Getting count for military rank ID: {}", militaryRankId);
        
        Long count = employeeService.getCountByMilitaryRank(militaryRankId);
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count by job
     */
    @GetMapping("/count/job/{jobId}")
    public ResponseEntity<Long> getCountByJob(@PathVariable Long jobId) {
        log.debug("Getting count for job ID: {}", jobId);
        
        Long count = employeeService.getCountByJob(jobId);
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of new recruits
     */
    @GetMapping("/count/new-recruits")
    public ResponseEntity<Long> getNewRecruitsCount() {
        log.debug("Getting count of new recruits");
        
        Long count = employeeService.getNewRecruitsCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of veteran employees
     */
    @GetMapping("/count/veterans")
    public ResponseEntity<Long> getVeteranEmployeesCount() {
        log.debug("Getting count of veteran employees");
        
        Long count = employeeService.getVeteranEmployeesCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of retirement eligible
     */
    @GetMapping("/count/retirement-eligible")
    public ResponseEntity<Long> getRetirementEligibleCount() {
        log.debug("Getting count of retirement eligible employees");
        
        Long count = employeeService.getRetirementEligibleCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count without job assignment
     */
    @GetMapping("/count/without-job")
    public ResponseEntity<Long> getCountWithoutJobAssignment() {
        log.debug("Getting count of employees without job assignment");
        
        Long count = employeeService.getCountWithoutJobAssignment();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get employee info with comprehensive details
     */
    @GetMapping("/{id}/info")
    public ResponseEntity<EmployeeInfoResponse> getEmployeeInfo(@PathVariable Long id) {
        log.debug("Getting comprehensive info for employee ID: {}", id);
        
        try {
            return employeeService.findOne(id)
                    .map(employeeDTO -> {
                        EmployeeInfoResponse response = EmployeeInfoResponse.builder()
                                .employeeMetadata(employeeDTO)
                                .displayName(employeeDTO.getDisplayName())
                                .fullNameAr(employeeDTO.getFullNameAr())
                                .fullNameLt(employeeDTO.getFullNameLt())
                                .militaryRankDesignation(employeeDTO.getMilitaryRankDesignation())
                                .militaryRankAbbreviation(employeeDTO.getMilitaryRankAbbreviation())
                                .jobDesignation(employeeDTO.getJobDesignation())
                                .jobStructure(employeeDTO.getJobStructure())
                                .yearsOfService(employeeDTO.getYearsOfService())
                                .hiringYear(employeeDTO.getHiringYear())
                                .serviceCategory(employeeDTO.getServiceCategory())
                                .isEligibleForRetirement(employeeDTO.isEligibleForRetirement())
                                .employeeStatus(employeeDTO.getEmployeeStatus())
                                .promotionEligibility(employeeDTO.getPromotionEligibility())
                                .hasCompleteProfile(employeeDTO.hasCompleteProfile())
                                .profileCompleteness(employeeDTO.getProfileCompleteness())
                                .employeeClassification(employeeDTO.getEmployeeClassification())
                                .commandAuthority(employeeDTO.getCommandAuthority())
                                .shortDisplay(employeeDTO.getShortDisplay())
                                .fullDisplay(employeeDTO.getFullDisplay())
                                .displayWithRank(employeeDTO.getDisplayWithRank())
                                .displayWithJob(employeeDTO.getDisplayWithJob())
                                .formalMilitaryDisplay(employeeDTO.getFormalMilitaryDisplay())
                                .serviceRecordSummary(employeeDTO.getServiceRecordSummary())
                                .needsProfileUpdate(employeeDTO.needsProfileUpdate())
                                .yearsToRetirement(employeeDTO.getYearsToRetirement())
                                .nextPromotionTimeline(employeeDTO.getNextPromotionTimeline())
                                .employeeSummary(employeeDTO.getEmployeeSummary())
                                .comparisonKey(employeeDTO.getComparisonKey())
                                .build();
                        
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());
                    
        } catch (Exception e) {
            log.error("Error getting employee info for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== RESPONSE DTOs ==========

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class EmployeeInfoResponse {
        private EmployeeDTO employeeMetadata;
        private String displayName;
        private String fullNameAr;
        private String fullNameLt;
        private String militaryRankDesignation;
        private String militaryRankAbbreviation;
        private String jobDesignation;
        private String jobStructure;
        private Integer yearsOfService;
        private Integer hiringYear;
        private String serviceCategory;
        private Boolean isEligibleForRetirement;
        private String employeeStatus;
        private String promotionEligibility;
        private Boolean hasCompleteProfile;
        private Double profileCompleteness;
        private String employeeClassification;
        private String commandAuthority;
        private String shortDisplay;
        private String fullDisplay;
        private String displayWithRank;
        private String displayWithJob;
        private String formalMilitaryDisplay;
        private String serviceRecordSummary;
        private Boolean needsProfileUpdate;
        private Integer yearsToRetirement;
        private String nextPromotionTimeline;
        private String employeeSummary;
        private String comparisonKey;
    }
}
