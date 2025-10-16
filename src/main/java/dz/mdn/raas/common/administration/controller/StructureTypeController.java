/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StructureTypeController
 *	@CreatedOn	: 10-16-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.controller;

import dz.mdn.raas.common.administration.service.StructureTypeService;
import dz.mdn.raas.common.administration.dto.StructureTypeDTO;

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
 * StructureType REST Controller
 * Handles structure type operations: create, get metadata, delete, get all
 * Based on exact StructureType model: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=acronymAr, F_05=acronymEn, F_06=acronymFr
 * F_03 (designationFr) has unique constraint and is required
 * F_06 (acronymFr) has unique constraint and is required
 * F_01 (designationAr), F_02 (designationEn), F_04 (acronymAr), F_05 (acronymEn) are optional
 */
@RestController
@RequestMapping("/structureType")
@RequiredArgsConstructor
@Slf4j
public class StructureTypeController {

    private final StructureTypeService structureTypeService;

    // ========== POST ONE STRUCTURE TYPE ==========

    /**
     * Create new structure type
     * Creates structure type with multilingual designations, acronyms, and organizational classification
     */
    @PostMapping
    public ResponseEntity<StructureTypeDTO> createStructureType(@Valid @RequestBody StructureTypeDTO structureTypeDTO) {
        log.info("Creating structure type with French designation: {} and acronym: {}, designations: AR={}, EN={}, acronyms: AR={}, EN={}", 
                structureTypeDTO.getDesignationFr(), structureTypeDTO.getAcronymFr(),
                structureTypeDTO.getDesignationAr(), structureTypeDTO.getDesignationEn(),
                structureTypeDTO.getAcronymAr(), structureTypeDTO.getAcronymEn());
        
        StructureTypeDTO createdStructureType = structureTypeService.createStructureType(structureTypeDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStructureType);
    }

    // ========== GET METADATA ==========

    /**
     * Get structure type metadata by ID
     * Returns structure type information with organizational classification and multilingual support
     */
    @GetMapping("/{id}")
    public ResponseEntity<StructureTypeDTO> getStructureTypeMetadata(@PathVariable Long id) {
        log.debug("Getting metadata for structure type ID: {}", id);
        
        StructureTypeDTO structureTypeMetadata = structureTypeService.getStructureTypeById(id);
        
        return ResponseEntity.ok(structureTypeMetadata);
    }

    /**
     * Get structure type by French designation (unique field F_03)
     */
    @GetMapping("/designation-fr/{designationFr}")
    public ResponseEntity<StructureTypeDTO> getStructureTypeByDesignationFr(@PathVariable String designationFr) {
        log.debug("Getting structure type by French designation: {}", designationFr);
        
        return structureTypeService.findByDesignationFr(designationFr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get structure type by French acronym (unique field F_06)
     */
    @GetMapping("/acronym-fr/{acronymFr}")
    public ResponseEntity<StructureTypeDTO> getStructureTypeByAcronymFr(@PathVariable String acronymFr) {
        log.debug("Getting structure type by French acronym: {}", acronymFr);
        
        return structureTypeService.findByAcronymFr(acronymFr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get structure type by Arabic designation (F_01)
     */
    @GetMapping("/designation-ar/{designationAr}")
    public ResponseEntity<StructureTypeDTO> getStructureTypeByDesignationAr(@PathVariable String designationAr) {
        log.debug("Getting structure type by Arabic designation: {}", designationAr);
        
        return structureTypeService.findByDesignationAr(designationAr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get structure type by English designation (F_02)
     */
    @GetMapping("/designation-en/{designationEn}")
    public ResponseEntity<StructureTypeDTO> getStructureTypeByDesignationEn(@PathVariable String designationEn) {
        log.debug("Getting structure type by English designation: {}", designationEn);
        
        return structureTypeService.findByDesignationEn(designationEn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get structure type by Arabic acronym (F_04)
     */
    @GetMapping("/acronym-ar/{acronymAr}")
    public ResponseEntity<StructureTypeDTO> getStructureTypeByAcronymAr(@PathVariable String acronymAr) {
        log.debug("Getting structure type by Arabic acronym: {}", acronymAr);
        
        return structureTypeService.findByAcronymAr(acronymAr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get structure type by English acronym (F_05)
     */
    @GetMapping("/acronym-en/{acronymEn}")
    public ResponseEntity<StructureTypeDTO> getStructureTypeByAcronymEn(@PathVariable String acronymEn) {
        log.debug("Getting structure type by English acronym: {}", acronymEn);
        
        return structureTypeService.findByAcronymEn(acronymEn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ========== DELETE ONE ==========

    /**
     * Delete structure type by ID
     * Removes structure type from the system
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStructureType(@PathVariable Long id) {
        log.info("Deleting structure type with ID: {}", id);
        
        structureTypeService.deleteStructureType(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== GET ALL ==========

    /**
     * Get all structure types with pagination
     * Returns list of all structure types ordered by French designation
     */
    @GetMapping
    public ResponseEntity<Page<StructureTypeDTO>> getAllStructureTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Getting all structure types - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                  page, size, sortBy, sortDir);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<StructureTypeDTO> structureTypes = structureTypeService.getAllStructureTypes(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get all structure types ordered by acronym
     */
    @GetMapping("/ordered-by-acronym")
    public ResponseEntity<Page<StructureTypeDTO>> getAllStructureTypesOrderByAcronym(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting all structure types ordered by acronym");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<StructureTypeDTO> structureTypes = structureTypeService.getAllStructureTypesOrderByAcronym(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    // ========== SEARCH ENDPOINTS ==========

    /**
     * Search structure types by designation (all languages)
     */
    @GetMapping("/search/designation")
    public ResponseEntity<Page<StructureTypeDTO>> searchStructureTypesByDesignation(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Searching structure types by designation with query: {}", query);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<StructureTypeDTO> structureTypes = structureTypeService.searchStructureTypesByDesignation(query, pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Search structure types by acronym (all languages)
     */
    @GetMapping("/search/acronym")
    public ResponseEntity<Page<StructureTypeDTO>> searchStructureTypesByAcronym(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "acronymFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Searching structure types by acronym with query: {}", query);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<StructureTypeDTO> structureTypes = structureTypeService.searchStructureTypesByAcronym(query, pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Search structure types by designation or acronym
     */
    @GetMapping("/search")
    public ResponseEntity<Page<StructureTypeDTO>> searchStructureTypes(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.debug("Searching structure types with query: {}", query);
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<StructureTypeDTO> structureTypes = structureTypeService.searchStructureTypes(query, pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    // ========== STRUCTURE CATEGORY ENDPOINTS ==========

    /**
     * Get multilingual structure types
     */
    @GetMapping("/multilingual")
    public ResponseEntity<Page<StructureTypeDTO>> getMultilingualStructureTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting multilingual structure types");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getMultilingualStructureTypes(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get command structures
     */
    @GetMapping("/command")
    public ResponseEntity<Page<StructureTypeDTO>> getCommandStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting command structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getCommandStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get administrative structures
     */
    @GetMapping("/administrative")
    public ResponseEntity<Page<StructureTypeDTO>> getAdministrativeStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting administrative structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getAdministrativeStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get operational structures
     */
    @GetMapping("/operational")
    public ResponseEntity<Page<StructureTypeDTO>> getOperationalStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting operational structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getOperationalStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get support structures
     */
    @GetMapping("/support")
    public ResponseEntity<Page<StructureTypeDTO>> getSupportStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting support structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getSupportStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get training structures
     */
    @GetMapping("/training")
    public ResponseEntity<Page<StructureTypeDTO>> getTrainingStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting training structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getTrainingStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get medical structures
     */
    @GetMapping("/medical")
    public ResponseEntity<Page<StructureTypeDTO>> getMedicalStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting medical structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getMedicalStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get technical structures
     */
    @GetMapping("/technical")
    public ResponseEntity<Page<StructureTypeDTO>> getTechnicalStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting technical structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getTechnicalStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get intelligence structures
     */
    @GetMapping("/intelligence")
    public ResponseEntity<Page<StructureTypeDTO>> getIntelligenceStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting intelligence structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getIntelligenceStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get communications structures
     */
    @GetMapping("/communications")
    public ResponseEntity<Page<StructureTypeDTO>> getCommunicationsStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting communications structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getCommunicationsStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get logistics structures
     */
    @GetMapping("/logistics")
    public ResponseEntity<Page<StructureTypeDTO>> getLogisticsStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting logistics structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getLogisticsStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    // ========== ORGANIZATIONAL LEVEL ENDPOINTS ==========

    /**
     * Get strategic level structures
     */
    @GetMapping("/level/strategic")
    public ResponseEntity<Page<StructureTypeDTO>> getStrategicLevelStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting strategic level structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getStrategicLevelStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get operational level structures
     */
    @GetMapping("/level/operational")
    public ResponseEntity<Page<StructureTypeDTO>> getOperationalLevelStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting operational level structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getOperationalLevelStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get tactical level structures
     */
    @GetMapping("/level/tactical")
    public ResponseEntity<Page<StructureTypeDTO>> getTacticalLevelStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting tactical level structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getTacticalLevelStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get unit level structures
     */
    @GetMapping("/level/unit")
    public ResponseEntity<Page<StructureTypeDTO>> getUnitLevelStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting unit level structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getUnitLevelStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    // ========== SPECIALIZED CLASSIFICATION ENDPOINTS ==========

    /**
     * Get security clearance structures
     */
    @GetMapping("/security-clearance")
    public ResponseEntity<Page<StructureTypeDTO>> getSecurityClearanceStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting security clearance structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getSecurityClearanceStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    /**
     * Get deployable structures
     */
    @GetMapping("/deployable")
    public ResponseEntity<Page<StructureTypeDTO>> getDeployableStructures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Getting deployable structures");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "designationFr"));
        Page<StructureTypeDTO> structureTypes = structureTypeService.getDeployableStructures(pageable);
        
        return ResponseEntity.ok(structureTypes);
    }

    // ========== UPDATE ENDPOINTS ==========

    /**
     * Update structure type metadata
     */
    @PutMapping("/{id}")
    public ResponseEntity<StructureTypeDTO> updateStructureType(
            @PathVariable Long id,
            @Valid @RequestBody StructureTypeDTO structureTypeDTO) {
        
        log.info("Updating structure type with ID: {}", id);
        
        StructureTypeDTO updatedStructureType = structureTypeService.updateStructureType(id, structureTypeDTO);
        
        return ResponseEntity.ok(updatedStructureType);
    }

    // ========== VALIDATION ENDPOINTS ==========

    /**
     * Check if structure type exists
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkStructureTypeExists(@PathVariable Long id) {
        log.debug("Checking existence of structure type ID: {}", id);
        
        boolean exists = structureTypeService.existsById(id);
        
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if structure type exists by French designation
     */
    @GetMapping("/exists/designation-fr/{designationFr}")
    public ResponseEntity<Boolean> checkStructureTypeExistsByDesignationFr(@PathVariable String designationFr) {
        log.debug("Checking existence by French designation: {}", designationFr);
        
        boolean exists = structureTypeService.existsByDesignationFr(designationFr);
        
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if structure type exists by French acronym
     */
    @GetMapping("/exists/acronym-fr/{acronymFr}")
    public ResponseEntity<Boolean> checkStructureTypeExistsByAcronymFr(@PathVariable String acronymFr) {
        log.debug("Checking existence by French acronym: {}", acronymFr);
        
        boolean exists = structureTypeService.existsByAcronymFr(acronymFr);
        
        return ResponseEntity.ok(exists);
    }

    // ========== STATISTICS ENDPOINTS ==========

    /**
     * Get total count of structure types
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getStructureTypesCount() {
        log.debug("Getting total count of structure types");
        
        Long count = structureTypeService.getTotalCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of command structures
     */
    @GetMapping("/count/command")
    public ResponseEntity<Long> getCommandStructuresCount() {
        log.debug("Getting count of command structures");
        
        Long count = structureTypeService.getCommandCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of administrative structures
     */
    @GetMapping("/count/administrative")
    public ResponseEntity<Long> getAdministrativeStructuresCount() {
        log.debug("Getting count of administrative structures");
        
        Long count = structureTypeService.getAdministrativeCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of operational structures
     */
    @GetMapping("/count/operational")
    public ResponseEntity<Long> getOperationalStructuresCount() {
        log.debug("Getting count of operational structures");
        
        Long count = structureTypeService.getOperationalCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of support structures
     */
    @GetMapping("/count/support")
    public ResponseEntity<Long> getSupportStructuresCount() {
        log.debug("Getting count of support structures");
        
        Long count = structureTypeService.getSupportCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get count of training structures
     */
    @GetMapping("/count/training")
    public ResponseEntity<Long> getTrainingStructuresCount() {
        log.debug("Getting count of training structures");
        
        Long count = structureTypeService.getTrainingCount();
        
        return ResponseEntity.ok(count);
    }

    /**
     * Get structure type info with comprehensive details
     */
    @GetMapping("/{id}/info")
    public ResponseEntity<StructureTypeInfoResponse> getStructureTypeInfo(@PathVariable Long id) {
        log.debug("Getting comprehensive info for structure type ID: {}", id);
        
        try {
            return structureTypeService.findOne(id)
                    .map(structureTypeDTO -> {
                        StructureTypeInfoResponse response = StructureTypeInfoResponse.builder()
                                .structureTypeMetadata(structureTypeDTO)
                                .hasArabicDesignation(structureTypeDTO.getDesignationAr() != null && !structureTypeDTO.getDesignationAr().trim().isEmpty())
                                .hasEnglishDesignation(structureTypeDTO.getDesignationEn() != null && !structureTypeDTO.getDesignationEn().trim().isEmpty())
                                .hasFrenchDesignation(structureTypeDTO.getDesignationFr() != null && !structureTypeDTO.getDesignationFr().trim().isEmpty())
                                .hasArabicAcronym(structureTypeDTO.getAcronymAr() != null && !structureTypeDTO.getAcronymAr().trim().isEmpty())
                                .hasEnglishAcronym(structureTypeDTO.getAcronymEn() != null && !structureTypeDTO.getAcronymEn().trim().isEmpty())
                                .hasFrenchAcronym(structureTypeDTO.getAcronymFr() != null && !structureTypeDTO.getAcronymFr().trim().isEmpty())
                                .isMultilingual(structureTypeDTO.isMultilingual())
                                .requiresSecurityClearance(structureTypeDTO.requiresSecurityClearance())
                                .hasOperationalRole(structureTypeDTO.hasOperationalRole())
                                .isDeployable(structureTypeDTO.isDeployable())
                                .isValid(structureTypeDTO.isValid())
                                .defaultDesignation(structureTypeDTO.getDefaultDesignation())
                                .defaultAcronym(structureTypeDTO.getDefaultAcronym())
                                .displayText(structureTypeDTO.getDisplayText())
                                .displayAcronym(structureTypeDTO.getDisplayAcronym())
                                .structureCategory(structureTypeDTO.getStructureCategory())
                                .organizationalLevel(structureTypeDTO.getOrganizationalLevel())
                                .structureSize(structureTypeDTO.getStructureSize())
                                .commandAuthority(structureTypeDTO.getCommandAuthority())
                                .structurePriority(structureTypeDTO.getStructurePriority())
                                .typicalPersonnelRange(structureTypeDTO.getTypicalPersonnelRange())
                                .structureMobility(structureTypeDTO.getStructureMobility())
                                .classificationLevel(structureTypeDTO.getClassificationLevel())
                                .reportingFrequency(structureTypeDTO.getReportingFrequency())
                                .establishmentRequirements(structureTypeDTO.getEstablishmentRequirements())
                                .shortDisplay(structureTypeDTO.getShortDisplay())
                                .fullDisplay(structureTypeDTO.getFullDisplay())
                                .displayWithCategory(structureTypeDTO.getDisplayWithCategory())
                                .displayWithAcronym(structureTypeDTO.getDisplayWithAcronym())
                                .availableLanguages(structureTypeDTO.getAvailableLanguages())
                                .comparisonKey(structureTypeDTO.getComparisonKey())
                                .build();
                        
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());
                    
        } catch (Exception e) {
            log.error("Error getting structure type info for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== RESPONSE DTOs ==========

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class StructureTypeInfoResponse {
        private StructureTypeDTO structureTypeMetadata;
        private Boolean hasArabicDesignation;
        private Boolean hasEnglishDesignation;
        private Boolean hasFrenchDesignation;
        private Boolean hasArabicAcronym;
        private Boolean hasEnglishAcronym;
        private Boolean hasFrenchAcronym;
        private Boolean isMultilingual;
        private Boolean requiresSecurityClearance;
        private Boolean hasOperationalRole;
        private Boolean isDeployable;
        private Boolean isValid;
        private String defaultDesignation;
        private String defaultAcronym;
        private String displayText;
        private String displayAcronym;
        private String structureCategory;
        private String organizationalLevel;
        private String structureSize;
        private String commandAuthority;
        private Integer structurePriority;
        private String typicalPersonnelRange;
        private String structureMobility;
        private String classificationLevel;
        private String reportingFrequency;
        private String[] establishmentRequirements;
        private String shortDisplay;
        private String fullDisplay;
        private String displayWithCategory;
        private String displayWithAcronym;
        private String[] availableLanguages;
        private String comparisonKey;
    }
}
