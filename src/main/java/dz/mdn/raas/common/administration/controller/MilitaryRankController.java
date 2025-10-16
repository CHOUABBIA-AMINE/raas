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
/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: MilitaryRankController
 *	@CreatedOn	: 10-16-2025
 *	@Type		: REST Controller
 *	@Layer		: Presentation
 *	@Package	: Common / Administration / Controller
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
 * Military Rank REST Controller
 * Handles military rank operations: create, get metadata, delete, get all
 * Based on exact MilitaryRank model: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=abbreviationAr, F_05=abbreviationEn, F_06=abbreviationFr, F_07=militaryCategory
 * F_03 (designationFr) has unique constraint and is required
 * F_06 (abbreviationFr) is required
 * F_07 (militaryCategory) is required foreign key
 * F_01, F_02, F_04, F_05 are optional
 */
@RestController
@RequestMapping("/api/v1/military-ranks")
@RequiredArgsConstructor
@Slf4j
public class MilitaryRankController {

    private final MilitaryRankService militaryRankService;

    // ========== POST ONE MILITARY RANK ==========

    /**
     * Create new military rank
     * Creates military rank with multilingual designations and abbreviations, and military category assignment
     */
    @PostMapping
    public ResponseEntity<MilitaryRankDTO> createMilitaryRank(@Valid @RequestBody MilitaryRankDTO militaryRankDTO) {
        log.info("Creating military rank with French designation: {} and designations: AR={}, EN={}, Abbreviations: FR={}, AR={}, EN={}, Category ID: {}", 
                militaryRankDTO.getDesignationFr(), militaryRankDTO.getDesignationAr(), 
                militaryRankDTO.getDesignationEn(), militaryRankDTO.getAbbreviationFr(),
                militaryRankDTO.getAbbreviationAr(), militaryRankDTO.getAbbreviationEn(),
                militaryRankDTO.getMilitaryCategoryId());
        
        MilitaryRankDTO createdMilitaryRank = militaryRankService.createMilitaryRank(militaryRankDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMilitaryRank);
    }

    // ========== GET METADATA ==========

    /**
     * Get military rank metadata by ID
     * Returns military rank information with multilingual designations, abbreviations, and military category context
     */
    @GetMapping("/{id}")
    public ResponseEntity<MilitaryRankDTO> getMilitaryRankMetadata(@PathVariable Long id) {
        log.debug("Getting metadata for military rank ID: {}", id);
        
        MilitaryRankDTO militaryRankMetadata = militaryRankService.getMilitaryRankById(id);
        
        return ResponseEntity.ok(militaryRankMetadata);
    }

    /**
     * Get military rank by French designation (F_03) - unique field
     */
    @GetMapping("/designation-fr/{designationFr}")
    public ResponseEntity<MilitaryRankDTO> getMilitaryRankByDesignationFr(@PathVariable String designationFr) {
        log.debug("Getting military rank by French designation: {}", designationFr);
        
        return militaryRankService.findByDesignationFr(designationFr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get military rank by French abbreviation (F_06)
     */
    @GetMapping("/abbreviation-fr/{abbreviationFr}")
    public ResponseEntity<MilitaryRankDTO> getMilitaryRankByAbbreviationFr(@PathVariable String abbreviationFr) {
        log.debug("Getting military rank by French abbreviation: {}", abbreviationFr);
        
        return militaryRankService.findByAbbreviationFr(abbreviationFr)
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
     * Get military ranks by military category ID (F_07)
     */
    @GetMapping("/military-category/{militaryCategoryId}")
    public ResponseEntity<Page<MilitaryRankDTO>> getMilitaryRanksByMilitaryCategory(
            @PathVariable Long militaryCategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting military ranks for military category ID: {}", militaryCategoryId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.findByMilitaryCategoryId(militaryCategoryId, pageable);
        
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
     * Get all military ranks ordered by military category and designation
     */
    @GetMapping("/ordered-by-category")
    public ResponseEntity<Page<MilitaryRankDTO>> getAllMilitaryRanksOrderedByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting all military ranks ordered by military category and designation");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getAllMilitaryRanksOrderedByCategory(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== SEARCH ENDPOINTS ==========

    /**
     * Search military ranks by designation or abbreviation (all languages)
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

    /**
     * Search military ranks with military category context
     */
    @GetMapping("/search/context")
    public ResponseEntity<Page<MilitaryRankDTO>> searchMilitaryRanksWithCategoryContext(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Searching military ranks with military category context for query: {}", query);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.searchMilitaryRanksWithCategoryContext(query, pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== MILITARY RANK CATEGORY ENDPOINTS ==========

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
     * Get officer military ranks
     */
    @GetMapping("/officers")
    public ResponseEntity<Page<MilitaryRankDTO>> getOfficerMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting officer military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getOfficerRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get senior officer military ranks
     */
    @GetMapping("/senior-officers")
    public ResponseEntity<Page<MilitaryRankDTO>> getSeniorOfficerMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting senior officer military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getSeniorOfficerRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get NCO military ranks
     */
    @GetMapping("/ncos")
    public ResponseEntity<Page<MilitaryRankDTO>> getNCOMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting NCO military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getNCORanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get enlisted military ranks
     */
    @GetMapping("/enlisted")
    public ResponseEntity<Page<MilitaryRankDTO>> getEnlistedMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting enlisted military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getEnlistedRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get general military ranks
     */
    @GetMapping("/generals")
    public ResponseEntity<Page<MilitaryRankDTO>> getGeneralMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting general military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getGeneralRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get command military ranks
     */
    @GetMapping("/command")
    public ResponseEntity<Page<MilitaryRankDTO>> getCommandMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting command military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getCommandRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== MILITARY RANK LEVEL ENDPOINTS ==========

    /**
     * Get top military ranks
     */
    @GetMapping("/level/top")
    public ResponseEntity<Page<MilitaryRankDTO>> getTopMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting top military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getTopRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get colonel military ranks
     */
    @GetMapping("/level/colonel")
    public ResponseEntity<Page<MilitaryRankDTO>> getColonelMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting colonel military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getColonelRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get major military ranks
     */
    @GetMapping("/level/major")
    public ResponseEntity<Page<MilitaryRankDTO>> getMajorMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting major military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getMajorRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get captain military ranks
     */
    @GetMapping("/level/captain")
    public ResponseEntity<Page<MilitaryRankDTO>> getCaptainMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting captain military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getCaptainRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Get lieutenant military ranks
     */
    @GetMapping("/level/lieutenant")
    public ResponseEntity<Page<MilitaryRankDTO>> getLieutenantMilitaryRanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting lieutenant military ranks");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getLieutenantRanks(pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== MILITARY CATEGORY-BASED ENDPOINTS ==========

    /**
     * Find military ranks by military category designation
     */
    @GetMapping("/category-designation/{categoryDesignation}")
    public ResponseEntity<Page<MilitaryRankDTO>> getMilitaryRanksByMilitaryCategoryDesignation(
            @PathVariable String categoryDesignation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Finding military ranks by military category designation: {}", categoryDesignation);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.findByMilitaryCategoryDesignation(categoryDesignation, pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    /**
     * Find military ranks by military category code
     */
    @GetMapping("/category-code/{categoryCode}")
    public ResponseEntity<Page<MilitaryRankDTO>> getMilitaryRanksByMilitaryCategoryCode(
            @PathVariable String categoryCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Finding military ranks by military category code: {}", categoryCode);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.findByMilitaryCategoryCode(categoryCode, pageable);
        
        return ResponseEntity.ok(militaryRanks);
    }

    // ========== ADMINISTRATIVE ENDPOINTS ==========

    /**
     * Get military ranks missing translations
     */
    @GetMapping("/missing-translations")
    public ResponseEntity<Page<MilitaryRankDTO>> getMilitaryRanksMissingTranslations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting military ranks missing translations");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<MilitaryRankDTO> militaryRanks = militaryRankService.getMilitaryRanksMissingTranslations(pageable);
        
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

    /**
     * Check if military rank exists by French abbreviation
     */
    @GetMapping("/exists/abbreviation-fr/{abbreviationFr}")
    public ResponseEntity<Boolean> checkMilitaryRankExistsByAbbreviationFr(@PathVariable String abbreviationFr) {
        log.debug("Checking existence by French abbreviation: {}", abbreviationFr);
        
        boolean exists = militaryRankService.existsByAbbreviationFr(abbreviationFr);
        
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
    @GetMapping("/count/military-category/{militaryCategoryId}")
    public ResponseEntity<Long> getCountByMilitaryCategory(@PathVariable Long militaryCategoryId) {
        log.debug("Getting count for military category ID: {}", militaryCategoryId);
        
        Long count = militaryRankService.getCountByMilitaryCategory(militaryCategoryId);
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of senior officer military ranks
     */
    @GetMapping("/count/senior-officers")
    public ResponseEntity<Long> getSeniorOfficerRanksCount() {
        log.debug("Getting count of senior officer military ranks");
        
        Long count = militaryRankService.getSeniorOfficerRanksCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of officer military ranks
     */
    @GetMapping("/count/officers")
    public ResponseEntity<Long> getOfficerRanksCount() {
        log.debug("Getting count of officer military ranks");
        
        Long count = militaryRankService.getOfficerRanksCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of NCO military ranks
     */
    @GetMapping("/count/ncos")
    public ResponseEntity<Long> getNCORanksCount() {
        log.debug("Getting count of NCO military ranks");
        
        Long count = militaryRankService.getNCORanksCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of enlisted military ranks
     */
    @GetMapping("/count/enlisted")
    public ResponseEntity<Long> getEnlistedRanksCount() {
        log.debug("Getting count of enlisted military ranks");
        
        Long count = militaryRankService.getEnlistedRanksCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of multilingual military ranks
     */
    @GetMapping("/count/multilingual")
    public ResponseEntity<Long> getMultilingualCount() {
        log.debug("Getting count of multilingual military ranks");
        
        Long count = militaryRankService.getMultilingualCount();
        
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
                                .defaultDesignation(militaryRankDTO.getDefaultDesignation())
                                .defaultAbbreviation(militaryRankDTO.getDefaultAbbreviation())
                                .displayText(militaryRankDTO.getDisplayText())
                                .displayAbbreviation(militaryRankDTO.getDisplayAbbreviation())
                                .isMultilingual(militaryRankDTO.isMultilingual())
                                .availableLanguages(militaryRankDTO.getAvailableLanguages())
                                .rankLevel(militaryRankDTO.getRankLevel())
                                .rankCategory(militaryRankDTO.getRankCategory())
                                .militaryCategoryDesignation(militaryRankDTO.getMilitaryCategoryDesignation())
                                .seniorityLevel(militaryRankDTO.getSeniorityLevel())
                                .hasCommandAuthority(militaryRankDTO.hasCommandAuthority())
                                .promotionRequirements(militaryRankDTO.getPromotionRequirements())
                                .shortDisplay(militaryRankDTO.getShortDisplay())
                                .fullDisplay(militaryRankDTO.getFullDisplay())
                                .comparisonKey(militaryRankDTO.getComparisonKey())
                                .displayWithCategory(militaryRankDTO.getDisplayWithCategory())
                                .displayWithAbbreviationAndCategory(militaryRankDTO.getDisplayWithAbbreviationAndCategory())
                                .formalMilitaryDisplay(militaryRankDTO.getFormalMilitaryDisplay())
                                .isCommissionedOfficer(militaryRankDTO.isCommissionedOfficer())
                                .isNonCommissionedOfficer(militaryRankDTO.isNonCommissionedOfficer())
                                .isEnlistedPersonnel(militaryRankDTO.isEnlistedPersonnel())
                                .minimumYearsForPromotion(militaryRankDTO.getMinimumYearsForPromotion())
                                .rankInsigniaDescription(militaryRankDTO.getRankInsigniaDescription())
                                .commandSpanDescription(militaryRankDTO.getCommandSpanDescription())
                                .typicalAssignment(militaryRankDTO.getTypicalAssignment())
                                .retirementEligibility(militaryRankDTO.getRetirementEligibility())
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
        private String defaultDesignation;
        private String defaultAbbreviation;
        private String displayText;
        private String displayAbbreviation;
        private Boolean isMultilingual;
        private String[] availableLanguages;
        private Integer rankLevel;
        private String rankCategory;
        private String militaryCategoryDesignation;
        private String militaryCategoryCode;
        private Integer seniorityLevel;
        private Boolean hasCommandAuthority;
        private String promotionRequirements;
        private String shortDisplay;
        private String fullDisplay;
        private String comparisonKey;
        private String displayWithCategory;
        private String displayWithAbbreviationAndCategory;
        private String formalMilitaryDisplay;
        private Boolean isCommissionedOfficer;
        private Boolean isNonCommissionedOfficer;
        private Boolean isEnlistedPersonnel;
        private Integer minimumYearsForPromotion;
        private String rankInsigniaDescription;
        private String commandSpanDescription;
        private String typicalAssignment;
        private String retirementEligibility;
    }
}