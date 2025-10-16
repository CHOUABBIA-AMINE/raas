/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryCategoryController
 *	@CreatedOn	: 10-16-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.controller;

import dz.mdn.raas.common.administration.service.MilitaryCategoryService;
import dz.mdn.raas.common.administration.dto.MilitaryCategoryDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MilitaryCategory REST Controller
 * Handles military category operations: create, get metadata, delete, get all
 * Based on exact MilitaryCategory model: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr
 * F_03 (designationFr) has unique constraint and is required
 * F_01 (designationAr) and F_02 (designationEn) are optional
 */
@RestController
@RequestMapping("/militaryCategory")
@RequiredArgsConstructor
@Slf4j
public class MilitaryCategoryController {

    private final MilitaryCategoryService militaryCategoryService;

    // ========== POST ONE MILITARY CATEGORY ==========

    /**
     * Create new military category
     * Creates military category with multilingual designations and military hierarchy classification
     */
    @PostMapping
    public ResponseEntity<MilitaryCategoryDTO> createMilitaryCategory(@Valid @RequestBody MilitaryCategoryDTO militaryCategoryDTO) {
        log.info("Creating military category with French designation: {} and designations: AR={}, EN={}", 
                militaryCategoryDTO.getDesignationFr(), militaryCategoryDTO.getDesignationAr(), 
                militaryCategoryDTO.getDesignationEn());
        
        MilitaryCategoryDTO createdMilitaryCategory = militaryCategoryService.createMilitaryCategory(militaryCategoryDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMilitaryCategory);
    }

    // ========== GET METADATA ==========

    /**
     * Get military category metadata by ID
     * Returns military category information with military hierarchy classification and multilingual support
     */
    @GetMapping("/{id}")
    public ResponseEntity<MilitaryCategoryDTO> getMilitaryCategoryMetadata(@PathVariable Long id) {
        log.debug("Getting metadata for military category ID: {}", id);
        
        MilitaryCategoryDTO militaryCategoryMetadata = militaryCategoryService.getMilitaryCategoryById(id);
        
        return ResponseEntity.ok(militaryCategoryMetadata);
    }

    /**
     * Get military category by French designation (unique field F_03)
     */
    @GetMapping("/designation-fr/{designationFr}")
    public ResponseEntity<MilitaryCategoryDTO> getMilitaryCategoryByDesignationFr(@PathVariable String designationFr) {
        log.debug("Getting military category by French designation: {}", designationFr);
        
        return militaryCategoryService.findByDesignationFr(designationFr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get military category by Arabic designation (F_01)
     */
    @GetMapping("/designation-ar/{designationAr}")
    public ResponseEntity<MilitaryCategoryDTO> getMilitaryCategoryByDesignationAr(@PathVariable String designationAr) {
        log.debug("Getting military category by Arabic designation: {}", designationAr);
        
        return militaryCategoryService.findByDesignationAr(designationAr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get military category by English designation (F_02)
     */
    @GetMapping("/designation-en/{designationEn}")
    public ResponseEntity<MilitaryCategoryDTO> getMilitaryCategoryByDesignationEn(@PathVariable String designationEn) {
        log.debug("Getting military category by English designation: {}", designationEn);
        
        return militaryCategoryService.findByDesignationEn(designationEn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== DELETE ONE ==========

    /**
     * Delete military category by ID
     * Removes military category from the system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMilitaryCategory(@PathVariable Long id) {
        log.info("Deleting military category with ID: {}", id);
        
        militaryCategoryService.deleteMilitaryCategory(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== GET ALL ==========

    /**
     * Get all military categories with pagination
     * Returns list of all military categories ordered by French designation
     */
    @GetMapping
    public ResponseEntity<Page<MilitaryCategoryDTO>> getAllMilitaryCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Getting all military categories - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                  page, size, sortBy, sortDir);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getAllMilitaryCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    // ========== SEARCH ENDPOINTS ==========

    /**
     * Search military categories by designation (all languages)
     */
    @GetMapping("/search")
    public ResponseEntity<Page<MilitaryCategoryDTO>> searchMilitaryCategories(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Searching military categories with query: {}", query);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.searchMilitaryCategories(query, pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    // ========== MILITARY CATEGORY TYPE ENDPOINTS ==========

    /**
     * Get multilingual military categories
     */
    @GetMapping("/multilingual")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getMultilingualMilitaryCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting multilingual military categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getMultilingualMilitaryCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get officer categories
     */
    @GetMapping("/officer")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getOfficerCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting officer categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getOfficerCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get NCO categories
     */
    @GetMapping("/nco")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getNCOCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting NCO categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getNCOCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get enlisted categories
     */
    @GetMapping("/enlisted")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getEnlistedCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting enlisted categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getEnlistedCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get specialist categories
     */
    @GetMapping("/specialist")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getSpecialistCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting specialist categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getSpecialistCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get medical categories
     */
    @GetMapping("/medical")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getMedicalCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting medical categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getMedicalCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get administrative categories
     */
    @GetMapping("/administrative")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getAdministrativeCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting administrative categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getAdministrativeCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get reserve categories
     */
    @GetMapping("/reserve")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getReserveCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting reserve categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getReserveCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get cadet categories
     */
    @GetMapping("/cadet")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getCadetCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting cadet categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getCadetCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get retired categories
     */
    @GetMapping("/retired")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getRetiredCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting retired categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getRetiredCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    // ========== MILITARY HIERARCHY ENDPOINTS ==========

    /**
     * Get active duty categories
     */
    @GetMapping("/active-duty")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getActiveDutyCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting active duty categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getActiveDutyCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get command categories
     */
    @GetMapping("/command")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getCommandCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting command categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getCommandCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get security clearance categories
     */
    @GetMapping("/security-clearance")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getSecurityClearanceCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting security clearance categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getSecurityClearanceCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get operational categories
     */
    @GetMapping("/operational")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getOperationalCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting operational categories");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getOperationalCategories(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    // ========== PERSONNEL CLASSIFICATION ENDPOINTS ==========

    /**
     * Get commissioned personnel
     */
    @GetMapping("/personnel/commissioned")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getCommissionedPersonnel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting commissioned personnel");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getCommissionedPersonnel(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get non-commissioned personnel
     */
    @GetMapping("/personnel/non-commissioned")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getNonCommissionedPersonnel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting non-commissioned personnel");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getNonCommissionedPersonnel(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    /**
     * Get enlisted personnel
     */
    @GetMapping("/personnel/enlisted")
    public ResponseEntity<Page<MilitaryCategoryDTO>> getEnlistedPersonnel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting enlisted personnel");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryCategoryDTO> militaryCategories = militaryCategoryService.getEnlistedPersonnel(pageable);
        
        return ResponseEntity.ok(militaryCategories);
    }

    // ========== UPDATE ENDPOINTS ==========

    /**
     * Update military category metadata
     */
    @PutMapping("/{id}")
    public ResponseEntity<MilitaryCategoryDTO> updateMilitaryCategory(
            @PathVariable Long id,
            @Valid @RequestBody MilitaryCategoryDTO militaryCategoryDTO) {
        
        log.info("Updating military category with ID: {}", id);
        
        MilitaryCategoryDTO updatedMilitaryCategory = militaryCategoryService.updateMilitaryCategory(id, militaryCategoryDTO);
        
        return ResponseEntity.ok(updatedMilitaryCategory);
    }

    // ========== VALIDATION ENDPOINTS ==========

    /**
     * Check if military category exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkMilitaryCategoryExists(@PathVariable Long id) {
        log.debug("Checking existence of military category ID: {}", id);
        
        boolean exists = militaryCategoryService.existsById(id);
        
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if military category exists by French designation
     */
    @GetMapping("/exists/designation-fr/{designationFr}")
    public ResponseEntity<Boolean> checkMilitaryCategoryExistsByDesignationFr(@PathVariable String designationFr) {
        log.debug("Checking existence by French designation: {}", designationFr);
        
        boolean exists = militaryCategoryService.existsByDesignationFr(designationFr);
        
        return ResponseEntity.ok(exists);
    }

    // ========== STATISTICS ENDPOINTS ==========

    /**
     * Get total count of military categories
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getMilitaryCategoriesCount() {
        log.debug("Getting total count of military categories");
        
        Long count = militaryCategoryService.getTotalCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of officer categories
     */
    @GetMapping("/count/officer")
    public ResponseEntity<Long> getOfficerCategoriesCount() {
        log.debug("Getting count of officer categories");
        
        Long count = militaryCategoryService.getOfficerCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of NCO categories
     */
    @GetMapping("/count/nco")
    public ResponseEntity<Long> getNCOCategoriesCount() {
        log.debug("Getting count of NCO categories");
        
        Long count = militaryCategoryService.getNCOCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of enlisted categories
     */
    @GetMapping("/count/enlisted")
    public ResponseEntity<Long> getEnlistedCategoriesCount() {
        log.debug("Getting count of enlisted categories");
        
        Long count = militaryCategoryService.getEnlistedCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of specialist categories
     */
    @GetMapping("/count/specialist")
    public ResponseEntity<Long> getSpecialistCategoriesCount() {
        log.debug("Getting count of specialist categories");
        
        Long count = militaryCategoryService.getSpecialistCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of medical categories
     */
    @GetMapping("/count/medical")
    public ResponseEntity<Long> getMedicalCategoriesCount() {
        log.debug("Getting count of medical categories");
        
        Long count = militaryCategoryService.getMedicalCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get military category info with comprehensive details
     */
    @GetMapping("/{id}/info")
    public ResponseEntity<MilitaryCategoryInfoResponse> getMilitaryCategoryInfo(@PathVariable Long id) {
        log.debug("Getting comprehensive info for military category ID: {}", id);
        
        try {
            return militaryCategoryService.findOne(id)
                    .map(militaryCategoryDTO -> {
                        MilitaryCategoryInfoResponse response = MilitaryCategoryInfoResponse.builder()
                                .militaryCategoryMetadata(militaryCategoryDTO)
                                .hasArabicDesignation(militaryCategoryDTO.getDesignationAr() != null && !militaryCategoryDTO.getDesignationAr().trim().isEmpty())
                                .hasEnglishDesignation(militaryCategoryDTO.getDesignationEn() != null && !militaryCategoryDTO.getDesignationEn().trim().isEmpty())
                                .hasFrenchDesignation(militaryCategoryDTO.getDesignationFr() != null && !militaryCategoryDTO.getDesignationFr().trim().isEmpty())
                                .isMultilingual(militaryCategoryDTO.isMultilingual())
                                .isOfficerCategory(militaryCategoryDTO.isOfficerCategory())
                                .isNCOCategory(militaryCategoryDTO.isNCOCategory())
                                .isEnlistedCategory(militaryCategoryDTO.isEnlistedCategory())
                                .isActiveDuty(militaryCategoryDTO.isActiveDuty())
                                .requiresSecurityClearance(militaryCategoryDTO.requiresSecurityClearance())
                                .hasOperationalRole(militaryCategoryDTO.hasOperationalRole())
                                .isValid(militaryCategoryDTO.isValid())
                                .defaultDesignation(militaryCategoryDTO.getDefaultDesignation())
                                .displayText(militaryCategoryDTO.getDisplayText())
                                .militaryCategoryType(militaryCategoryDTO.getMilitaryCategoryType())
                                .hierarchyLevel(militaryCategoryDTO.getHierarchyLevel())
                                .commandAuthority(militaryCategoryDTO.getCommandAuthority())
                                .categoryPriority(militaryCategoryDTO.getCategoryPriority())
                                .trainingRequirement(militaryCategoryDTO.getTrainingRequirement())
                                .deploymentEligibility(militaryCategoryDTO.getDeploymentEligibility())
                                .personnelClassification(militaryCategoryDTO.getPersonnelClassification())
                                .categoryAbbreviation(militaryCategoryDTO.getCategoryAbbreviation())
                                .serviceBranches(militaryCategoryDTO.getServiceBranches())
                                .shortDisplay(militaryCategoryDTO.getShortDisplay())
                                .fullDisplay(militaryCategoryDTO.getFullDisplay())
                                .displayWithType(militaryCategoryDTO.getDisplayWithType())
                                .availableLanguages(militaryCategoryDTO.getAvailableLanguages())
                                .comparisonKey(militaryCategoryDTO.getComparisonKey())
                                .build();
                        
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());
                    
        } catch (Exception e) {
            log.error("Error getting military category info for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== RESPONSE DTOs ==========

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class MilitaryCategoryInfoResponse {
        private MilitaryCategoryDTO militaryCategoryMetadata;
        private Boolean hasArabicDesignation;
        private Boolean hasEnglishDesignation;
        private Boolean hasFrenchDesignation;
        private Boolean isMultilingual;
        private Boolean isOfficerCategory;
        private Boolean isNCOCategory;
        private Boolean isEnlistedCategory;
        private Boolean isActiveDuty;
        private Boolean requiresSecurityClearance;
        private Boolean hasOperationalRole;
        private Boolean isValid;
        private String defaultDesignation;
        private String displayText;
        private String militaryCategoryType;
        private String hierarchyLevel;
        private String commandAuthority;
        private Integer categoryPriority;
        private String trainingRequirement;
        private String deploymentEligibility;
        private String personnelClassification;
        private String categoryAbbreviation;
        private String[] serviceBranches;
        private String shortDisplay;
        private String fullDisplay;
        private String displayWithType;
        private String[] availableLanguages;
        private String comparisonKey;
    }
}
