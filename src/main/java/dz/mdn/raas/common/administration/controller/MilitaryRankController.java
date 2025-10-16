/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryRankController
 *	@CreatedOn	: 10-16-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.controller;

import dz.mdn.raas.common.administration.service.MilitaryRankService;
import dz.mdn.raas.common.administration.dto.MilitaryRankDTO;

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
 * MilitaryRank REST Controller
 * Handles military rank operations: create, get metadata, delete, get all
 * Based on exact MilitaryRank model: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=militaryCategory
 * F_03 (designationFr) has unique constraint and is required
 * F_01 (designationAr) and F_02 (designationEn) are optional
 * F_04 (militaryCategory) is required foreign key
 */
@RestController
@RequestMapping("/militaryRank")
@RequiredArgsConstructor
@Slf4j
public class MilitaryRankController {

    private final MilitaryRankService militaryRankService;

    // ========== POST ONE MILITARY RANK ==========

    /**
     * Create new military rank
     * Creates military rank with multilingual designations and military hierarchy classification
     */
    @PostMapping
    public ResponseEntity<MilitaryRankDTO> createMilitaryRank(@Valid @RequestBody MilitaryRankDTO militaryRankDTO) {
        log.info("Creating military rank with French designation: {} and designations: AR={}, EN={}, Category ID: {}", 
                militaryRankDTO.getDesignationFr(), militaryRankDTO.getDesignationAr(), 
                militaryRankDTO.getDesignationEn(), militaryRankDTO.getMilitaryCategoryId());
        
        MilitaryRankDTO createdMilitaryRank = militaryRankService.createMilitaryRank(militaryRankDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMilitaryRank);
    }

    // ========== GET METADATA ==========

    /**
     * Get military rank metadata by ID
     * Returns military rank information with military hierarchy classification and multilingual support
     */
    @GetMapping("/{id}")
    public ResponseEntity<MilitaryRankDTO> getMilitaryRankMetadata(@PathVariable Long id) {
        log.debug("Getting metadata for military rank ID: {}", id);
        
        MilitaryRankDTO militaryRankMetadata = militaryRankService.getMilitaryRankById(id);
        
        return ResponseEntity.ok(militaryRankMetadata);
    }

    /**
     * Get military rank by French designation (unique field F_03)
     */
    @GetMapping("/designation-fr/{designationFr}")
    public ResponseEntity<MilitaryRankDTO> getMilitaryRankByDesignationFr(@PathVariable String designationFr) {
        log.debug("Getting military rank by French designation: {}", designationFr);
        
        return militaryRankService.findByDesignationFr(designationFr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get military rank by Arabic designation (F_01)
     */
    @GetMapping("/designation-ar/{designationAr}")
    public ResponseEntity<MilitaryRankDTO> getMilitaryRankByDesignationAr(@PathVariable String designationAr) {
        log.debug("Getting military rank by Arabic designation: {}", designationAr);
        
        return militaryRankService.findByDesignationAr(designationAr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get military rank by English designation (F_02)
     */
    @GetMapping("/designation-en/{designationEn}")
    public ResponseEntity<MilitaryRankDTO> getMilitaryRankByDesignationEn(@PathVariable String designationEn) {
        log.debug("Getting military rank by English designation: {}", designationEn);
        
        return militaryRankService.findByDesignationEn(designationEn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get military ranks by military category ID (F_04)
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<MilitaryRankDTO>> getMilitaryRanksByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting military ranks for category ID: {}", categoryId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.findByMilitaryCategoryId(categoryId, pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== DELETE ONE ==========

    /**
     * Delete military rank by ID
     * Removes military rank from the system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMilitaryRank(@PathVariable Long id) {
        log.info("Deleting military rank with ID: {}", id);
        
        militaryRankService.deleteMilitaryRank(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== GET ALL ==========

    /**
     * Get all military ranks with pagination
     * Returns list of all military ranks ordered by French designation
     */
    @GetMapping
    public ResponseEntity<Page<MilitaryRankDTO>> getAllMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Getting all military ranks - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                  page, size, sortBy, sortDir);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getAllMilitaryRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get all military ranks ordered by category and designation
     */
    @GetMapping("/ordered-by-category")
    public ResponseEntity<Page<MilitaryRankDTO>> getAllMilitaryRanksOrderedByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting all military ranks ordered by category and designation");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getAllMilitaryRanksOrderedByCategory(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== SEARCH ENDPOINTS ==========

    /**
     * Search military ranks by designation (all languages)
     */
    @GetMapping("/search")
    public ResponseEntity<Page<MilitaryRankDTO>> searchMilitaryRanks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Searching military ranks with query: {}", query);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.searchMilitaryRanks(query, pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== MILITARY RANK LEVEL ENDPOINTS ==========

    /**
     * Get multilingual military ranks
     */
    @GetMapping("/multilingual")
    public ResponseEntity<Page<MilitaryRankDTO>> getMultilingualMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting multilingual military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getMultilingualMilitaryRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get general officer ranks
     */
    @GetMapping("/general-officer")
    public ResponseEntity<Page<MilitaryRankDTO>> getGeneralOfficerRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting general officer ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getGeneralOfficerRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get field officer ranks
     */
    @GetMapping("/field-officer")
    public ResponseEntity<Page<MilitaryRankDTO>> getFieldOfficerRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting field officer ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getFieldOfficerRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get company officer ranks
     */
    @GetMapping("/company-officer")
    public ResponseEntity<Page<MilitaryRankDTO>> getCompanyOfficerRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting company officer ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getCompanyOfficerRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get senior NCO ranks
     */
    @GetMapping("/senior-nco")
    public ResponseEntity<Page<MilitaryRankDTO>> getSeniorNCORanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting senior NCO ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getSeniorNCORanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get junior NCO ranks
     */
    @GetMapping("/junior-nco")
    public ResponseEntity<Page<MilitaryRankDTO>> getJuniorNCORanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting junior NCO ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getJuniorNCORanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get enlisted ranks
     */
    @GetMapping("/enlisted")
    public ResponseEntity<Page<MilitaryRankDTO>> getEnlistedRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting enlisted ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getEnlistedRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get cadet ranks
     */
    @GetMapping("/cadet")
    public ResponseEntity<Page<MilitaryRankDTO>> getCadetRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting cadet ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getCadetRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== MILITARY HIERARCHY ENDPOINTS ==========

    /**
     * Get all officer ranks
     */
    @GetMapping("/officer")
    public ResponseEntity<Page<MilitaryRankDTO>> getOfficerRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting officer ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getOfficerRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get all NCO ranks
     */
    @GetMapping("/nco")
    public ResponseEntity<Page<MilitaryRankDTO>> getNCORanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting NCO ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getNCORanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get security clearance ranks
     */
    @GetMapping("/security-clearance")
    public ResponseEntity<Page<MilitaryRankDTO>> getSecurityClearanceRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting security clearance ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getSecurityClearanceRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get promotion eligible ranks
     */
    @GetMapping("/promotion-eligible")
    public ResponseEntity<Page<MilitaryRankDTO>> getPromotionEligibleRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting promotion eligible ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getPromotionEligibleRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== SERVICE BRANCH ENDPOINTS ==========

    /**
     * Get Army ranks
     */
    @GetMapping("/army")
    public ResponseEntity<Page<MilitaryRankDTO>> getArmyRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting Army ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getArmyRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get Navy ranks
     */
    @GetMapping("/navy")
    public ResponseEntity<Page<MilitaryRankDTO>> getNavyRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting Navy ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getNavyRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get Air Force ranks
     */
    @GetMapping("/air-force")
    public ResponseEntity<Page<MilitaryRankDTO>> getAirForceRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting Air Force ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getAirForceRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== COMMAND LEVEL ENDPOINTS ==========

    /**
     * Get strategic command ranks
     */
    @GetMapping("/command/strategic")
    public ResponseEntity<Page<MilitaryRankDTO>> getStrategicCommandRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting strategic command ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getStrategicCommandRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get operational command ranks
     */
    @GetMapping("/command/operational")
    public ResponseEntity<Page<MilitaryRankDTO>> getOperationalCommandRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting operational command ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getOperationalCommandRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get tactical command ranks
     */
    @GetMapping("/command/tactical")
    public ResponseEntity<Page<MilitaryRankDTO>> getTacticalCommandRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting tactical command ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getTacticalCommandRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Find by category designation
     */
    @GetMapping("/category-designation/{categoryDesignation}")
    public ResponseEntity<Page<MilitaryRankDTO>> findByCategoryDesignation(
            @PathVariable String categoryDesignation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Finding military ranks by category designation: {}", categoryDesignation);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.findByCategoryDesignation(categoryDesignation, pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== UPDATE ENDPOINTS ==========

    /**
     * Update military rank metadata
     */
    @PutMapping("/{id}")
    public ResponseEntity<MilitaryRankDTO> updateMilitaryRank(
            @PathVariable Long id,
            @Valid @RequestBody MilitaryRankDTO militaryRankDTO) {
        
        log.info("Updating military rank with ID: {}", id);
        
        MilitaryRankDTO updatedMilitaryRank = militaryRankService.updateMilitaryRank(id, militaryRankDTO);
        
        return ResponseEntity.ok(updatedMilitaryRank);
    }

    // ========== VALIDATION ENDPOINTS ==========

    /**
     * Check if military rank exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkMilitaryRankExists(@PathVariable Long id) {
        log.debug("Checking existence of military rank ID: {}", id);
        
        boolean exists = militaryRankService.existsById(id);
        
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if military rank exists by French designation
     */
    @GetMapping("/exists/designation-fr/{designationFr}")
    public ResponseEntity<Boolean> checkMilitaryRankExistsByDesignationFr(@PathVariable String designationFr) {
        log.debug("Checking existence by French designation: {}", designationFr);
        
        boolean exists = militaryRankService.existsByDesignationFr(designationFr);
        
        return ResponseEntity.ok(exists);
    }

    // ========== STATISTICS ENDPOINTS ==========

    /**
     * Get total count of military ranks
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getMilitaryRanksCount() {
        log.debug("Getting total count of military ranks");
        
        Long count = militaryRankService.getTotalCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count by military category
     */
    @GetMapping("/count/category/{categoryId}")
    public ResponseEntity<Long> getCountByCategory(@PathVariable Long categoryId) {
        log.debug("Getting count for category ID: {}", categoryId);
        
        Long count = militaryRankService.getCountByCategory(categoryId);
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of general officer ranks
     */
    @GetMapping("/count/general-officer")
    public ResponseEntity<Long> getGeneralOfficerCount() {
        log.debug("Getting count of general officer ranks");
        
        Long count = militaryRankService.getGeneralOfficerCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of field officer ranks
     */
    @GetMapping("/count/field-officer")
    public ResponseEntity<Long> getFieldOfficerCount() {
        log.debug("Getting count of field officer ranks");
        
        Long count = militaryRankService.getFieldOfficerCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of company officer ranks
     */
    @GetMapping("/count/company-officer")
    public ResponseEntity<Long> getCompanyOfficerCount() {
        log.debug("Getting count of company officer ranks");
        
        Long count = militaryRankService.getCompanyOfficerCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of NCO ranks
     */
    @GetMapping("/count/nco")
    public ResponseEntity<Long> getNCOCount() {
        log.debug("Getting count of NCO ranks");
        
        Long count = militaryRankService.getNCOCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of enlisted ranks
     */
    @GetMapping("/count/enlisted")
    public ResponseEntity<Long> getEnlistedCount() {
        log.debug("Getting count of enlisted ranks");
        
        Long count = militaryRankService.getEnlistedCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get military rank info with comprehensive details
     */
    @GetMapping("/{id}/info")
    public ResponseEntity<MilitaryRankInfoResponse> getMilitaryRankInfo(@PathVariable Long id) {
        log.debug("Getting comprehensive info for military rank ID: {}", id);
        
        try {
            return militaryRankService.findOne(id)
                    .map(militaryRankDTO -> {
                        MilitaryRankInfoResponse response = MilitaryRankInfoResponse.builder()
                                .militaryRankMetadata(militaryRankDTO)
                                .hasArabicDesignation(militaryRankDTO.getDesignationAr() != null && !militaryRankDTO.getDesignationAr().trim().isEmpty())
                                .hasEnglishDesignation(militaryRankDTO.getDesignationEn() != null && !militaryRankDTO.getDesignationEn().trim().isEmpty())
                                .hasFrenchDesignation(militaryRankDTO.getDesignationFr() != null && !militaryRankDTO.getDesignationFr().trim().isEmpty())
                                .isMultilingual(militaryRankDTO.isMultilingual())
                                .isOfficerRank(militaryRankDTO.isOfficerRank())
                                .isNCORank(militaryRankDTO.isNCORank())
                                .isEnlistedRank(militaryRankDTO.isEnlistedRank())
                                .isPromotionEligible(militaryRankDTO.isPromotionEligible())
                                .requiresSecurityClearance(militaryRankDTO.requiresSecurityClearance())
                                .isValid(militaryRankDTO.isValid())
                                .defaultDesignation(militaryRankDTO.getDefaultDesignation())
                                .displayText(militaryRankDTO.getDisplayText())
                                .rankLevel(militaryRankDTO.getRankLevel())
                                .rankOrder(militaryRankDTO.getRankOrder())
                                .commandLevel(militaryRankDTO.getCommandLevel())
                                .seniorityLevel(militaryRankDTO.getSeniorityLevel())
                                .insigniaDescription(militaryRankDTO.getInsigniaDescription())
                                .rankAbbreviation(militaryRankDTO.getRankAbbreviation())
                                .typicalServiceYears(militaryRankDTO.getTypicalServiceYears())
                                .payGrade(militaryRankDTO.getPayGrade())
                                .militaryCategoryDesignation(militaryRankDTO.getMilitaryCategoryDesignation())
                                .shortDisplay(militaryRankDTO.getShortDisplay())
                                .fullDisplay(militaryRankDTO.getFullDisplay())
                                .displayWithLevel(militaryRankDTO.getDisplayWithLevel())
                                .availableLanguages(militaryRankDTO.getAvailableLanguages())
                                .comparisonKey(militaryRankDTO.getComparisonKey())
                                .build();
                        
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());
                    
        } catch (Exception e) {
            log.error("Error getting military rank info for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== RESPONSE DTOs ==========

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class MilitaryRankInfoResponse {
        private MilitaryRankDTO militaryRankMetadata;
        private Boolean hasArabicDesignation;
        private Boolean hasEnglishDesignation;
        private Boolean hasFrenchDesignation;
        private Boolean isMultilingual;
        private Boolean isOfficerRank;
        private Boolean isNCORank;
        private Boolean isEnlistedRank;
        private Boolean isPromotionEligible;
        private Boolean requiresSecurityClearance;
        private Boolean isValid;
        private String defaultDesignation;
        private String displayText;
        private String rankLevel;
        private Integer rankOrder;
        private String commandLevel;
        private Integer seniorityLevel;
        private String insigniaDescription;
        private String rankAbbreviation;
        private String typicalServiceYears;
        private String payGrade;
        private String militaryCategoryDesignation;
        private String shortDisplay;
        private String fullDisplay;
        private String displayWithLevel;
        private String[] availableLanguages;
        private String comparisonKey;
    }
}
