/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryCategoryService
 *	@CreatedOn	: 10-16-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.service;

import dz.mdn.raas.common.administration.model.MilitaryCategory;
import dz.mdn.raas.common.administration.repository.MilitaryCategoryRepository;
import dz.mdn.raas.common.administration.dto.MilitaryCategoryDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * MilitaryCategory Service with CRUD operations
 * Handles military category management operations with multilingual support
 * Based on exact field names: F_01=designationAr, F_02=designationEn, F_03=designationFr
 * F_03 (designationFr) has unique constraint and is required
 * F_01 (designationAr) and F_02 (designationEn) are optional
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MilitaryCategoryService {

    private final MilitaryCategoryRepository militaryCategoryRepository;

    // ========== CREATE OPERATIONS ==========

    /**
     * Create new military category
     */
    public MilitaryCategoryDTO createMilitaryCategory(MilitaryCategoryDTO militaryCategoryDTO) {
        log.info("Creating military category with French designation: {} and designations: AR={}, EN={}", 
                militaryCategoryDTO.getDesignationFr(), militaryCategoryDTO.getDesignationAr(), 
                militaryCategoryDTO.getDesignationEn());

        // Validate required fields
        validateRequiredFields(militaryCategoryDTO, "create");

        // Check for unique constraint violation
        validateUniqueConstraints(militaryCategoryDTO, null);

        // Create entity with exact field mapping
        MilitaryCategory militaryCategory = new MilitaryCategory();
        militaryCategory.setDesignationAr(militaryCategoryDTO.getDesignationAr()); // F_01
        militaryCategory.setDesignationEn(militaryCategoryDTO.getDesignationEn()); // F_02
        militaryCategory.setDesignationFr(militaryCategoryDTO.getDesignationFr()); // F_03

        MilitaryCategory savedMilitaryCategory = militaryCategoryRepository.save(militaryCategory);
        log.info("Successfully created military category with ID: {}", savedMilitaryCategory.getId());

        return MilitaryCategoryDTO.fromEntity(savedMilitaryCategory);
    }

    // ========== READ OPERATIONS ==========

    /**
     * Get military category by ID
     */
    @Transactional(readOnly = true)
    public MilitaryCategoryDTO getMilitaryCategoryById(Long id) {
        log.debug("Getting military category with ID: {}", id);

        MilitaryCategory militaryCategory = militaryCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Military category not found with ID: " + id));

        return MilitaryCategoryDTO.fromEntity(militaryCategory);
    }

    /**
     * Get military category entity by ID
     */
    @Transactional(readOnly = true)
    public MilitaryCategory getMilitaryCategoryEntityById(Long id) {
        return militaryCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Military category not found with ID: " + id));
    }

    /**
     * Find military category by French designation (unique field F_03)
     */
    @Transactional(readOnly = true)
    public Optional<MilitaryCategoryDTO> findByDesignationFr(String designationFr) {
        log.debug("Finding military category with French designation: {}", designationFr);

        return militaryCategoryRepository.findByDesignationFr(designationFr)
                .map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Find military category by Arabic designation (F_01)
     */
    @Transactional(readOnly = true)
    public Optional<MilitaryCategoryDTO> findByDesignationAr(String designationAr) {
        log.debug("Finding military category with Arabic designation: {}", designationAr);

        return militaryCategoryRepository.findByDesignationAr(designationAr)
                .map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Find military category by English designation (F_02)
     */
    @Transactional(readOnly = true)
    public Optional<MilitaryCategoryDTO> findByDesignationEn(String designationEn) {
        log.debug("Finding military category with English designation: {}", designationEn);

        return militaryCategoryRepository.findByDesignationEn(designationEn)
                .map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get all military categories with pagination
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getAllMilitaryCategories(Pageable pageable) {
        log.debug("Getting all military categories with pagination");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findAllOrderByDesignationFr(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Find one military category by ID
     */
    @Transactional(readOnly = true)
    public Optional<MilitaryCategoryDTO> findOne(Long id) {
        log.debug("Finding military category by ID: {}", id);

        return militaryCategoryRepository.findById(id)
                .map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Search military categories by designation
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> searchMilitaryCategories(String searchTerm, Pageable pageable) {
        log.debug("Searching military categories with term: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllMilitaryCategories(pageable);
        }

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.searchByDesignation(searchTerm.trim(), pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get multilingual military categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getMultilingualMilitaryCategories(Pageable pageable) {
        log.debug("Getting multilingual military categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findMultilingualMilitaryCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get officer categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getOfficerCategories(Pageable pageable) {
        log.debug("Getting officer categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findOfficerCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get NCO categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getNCOCategories(Pageable pageable) {
        log.debug("Getting NCO categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findNCOCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get enlisted categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getEnlistedCategories(Pageable pageable) {
        log.debug("Getting enlisted categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findEnlistedCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get specialist categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getSpecialistCategories(Pageable pageable) {
        log.debug("Getting specialist categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findSpecialistCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get medical categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getMedicalCategories(Pageable pageable) {
        log.debug("Getting medical categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findMedicalCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get administrative categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getAdministrativeCategories(Pageable pageable) {
        log.debug("Getting administrative categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findAdministrativeCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get reserve categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getReserveCategories(Pageable pageable) {
        log.debug("Getting reserve categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findReserveCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get cadet categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getCadetCategories(Pageable pageable) {
        log.debug("Getting cadet categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findCadetCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get retired categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getRetiredCategories(Pageable pageable) {
        log.debug("Getting retired categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findRetiredCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get active duty categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getActiveDutyCategories(Pageable pageable) {
        log.debug("Getting active duty categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findActiveDutyCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get command categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getCommandCategories(Pageable pageable) {
        log.debug("Getting command categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findCommandCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get security clearance categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getSecurityClearanceCategories(Pageable pageable) {
        log.debug("Getting security clearance categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findSecurityClearanceCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get operational categories
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getOperationalCategories(Pageable pageable) {
        log.debug("Getting operational categories");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findOperationalCategories(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get commissioned personnel
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getCommissionedPersonnel(Pageable pageable) {
        log.debug("Getting commissioned personnel");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findCommissionedPersonnel(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get non-commissioned personnel
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getNonCommissionedPersonnel(Pageable pageable) {
        log.debug("Getting non-commissioned personnel");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findNonCommissionedPersonnel(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    /**
     * Get enlisted personnel
     */
    @Transactional(readOnly = true)
    public Page<MilitaryCategoryDTO> getEnlistedPersonnel(Pageable pageable) {
        log.debug("Getting enlisted personnel");

        Page<MilitaryCategory> militaryCategories = militaryCategoryRepository.findEnlistedPersonnel(pageable);
        return militaryCategories.map(MilitaryCategoryDTO::fromEntity);
    }

    // ========== UPDATE OPERATIONS ==========

    /**
     * Update military category
     */
    public MilitaryCategoryDTO updateMilitaryCategory(Long id, MilitaryCategoryDTO militaryCategoryDTO) {
        log.info("Updating military category with ID: {}", id);

        MilitaryCategory existingMilitaryCategory = getMilitaryCategoryEntityById(id);

        // Validate required fields
        validateRequiredFields(militaryCategoryDTO, "update");

        // Check for unique constraint violation (excluding current record)
        validateUniqueConstraints(militaryCategoryDTO, id);

        // Update fields with exact field mapping
        existingMilitaryCategory.setDesignationAr(militaryCategoryDTO.getDesignationAr()); // F_01
        existingMilitaryCategory.setDesignationEn(militaryCategoryDTO.getDesignationEn()); // F_02
        existingMilitaryCategory.setDesignationFr(militaryCategoryDTO.getDesignationFr()); // F_03

        MilitaryCategory updatedMilitaryCategory = militaryCategoryRepository.save(existingMilitaryCategory);
        log.info("Successfully updated military category with ID: {}", id);

        return MilitaryCategoryDTO.fromEntity(updatedMilitaryCategory);
    }

    // ========== DELETE OPERATIONS ==========

    /**
     * Delete military category
     */
    public void deleteMilitaryCategory(Long id) {
        log.info("Deleting military category with ID: {}", id);

        MilitaryCategory militaryCategory = getMilitaryCategoryEntityById(id);
        militaryCategoryRepository.delete(militaryCategory);

        log.info("Successfully deleted military category with ID: {}", id);
    }

    /**
     * Delete military category by ID (direct)
     */
    public void deleteMilitaryCategoryById(Long id) {
        log.info("Deleting military category by ID: {}", id);

        if (!militaryCategoryRepository.existsById(id)) {
            throw new RuntimeException("Military category not found with ID: " + id);
        }

        militaryCategoryRepository.deleteById(id);
        log.info("Successfully deleted military category with ID: {}", id);
    }

    // ========== UTILITY METHODS ==========

    /**
     * Check if military category exists
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return militaryCategoryRepository.existsById(id);
    }

    /**
     * Check if military category exists by French designation
     */
    @Transactional(readOnly = true)
    public boolean existsByDesignationFr(String designationFr) {
        return militaryCategoryRepository.existsByDesignationFr(designationFr);
    }

    /**
     * Get total count of military categories
     */
    @Transactional(readOnly = true)
    public Long getTotalCount() {
        return militaryCategoryRepository.countAllMilitaryCategories();
    }

    /**
     * Get count of officer categories
     */
    @Transactional(readOnly = true)
    public Long getOfficerCount() {
        return militaryCategoryRepository.countOfficerCategories();
    }

    /**
     * Get count of NCO categories
     */
    @Transactional(readOnly = true)
    public Long getNCOCount() {
        return militaryCategoryRepository.countNCOCategories();
    }

    /**
     * Get count of enlisted categories
     */
    @Transactional(readOnly = true)
    public Long getEnlistedCount() {
        return militaryCategoryRepository.countEnlistedCategories();
    }

    /**
     * Get count of specialist categories
     */
    @Transactional(readOnly = true)
    public Long getSpecialistCount() {
        return militaryCategoryRepository.countSpecialistCategories();
    }

    /**
     * Get count of medical categories
     */
    @Transactional(readOnly = true)
    public Long getMedicalCount() {
        return militaryCategoryRepository.countMedicalCategories();
    }

    // ========== VALIDATION METHODS ==========

    /**
     * Validate required fields
     */
    private void validateRequiredFields(MilitaryCategoryDTO militaryCategoryDTO, String operation) {
        if (militaryCategoryDTO.getDesignationFr() == null || militaryCategoryDTO.getDesignationFr().trim().isEmpty()) {
            throw new RuntimeException("French designation is required for " + operation);
        }
    }

    /**
     * Validate unique constraints
     */
    private void validateUniqueConstraints(MilitaryCategoryDTO militaryCategoryDTO, Long excludeId) {
        // Check French designation uniqueness (F_03)
        if (excludeId == null) {
            if (militaryCategoryRepository.existsByDesignationFr(militaryCategoryDTO.getDesignationFr())) {
                throw new RuntimeException("Military category with French designation '" + militaryCategoryDTO.getDesignationFr() + "' already exists");
            }
        } else {
            if (militaryCategoryRepository.existsByDesignationFrAndIdNot(militaryCategoryDTO.getDesignationFr(), excludeId)) {
                throw new RuntimeException("Another military category with French designation '" + militaryCategoryDTO.getDesignationFr() + "' already exists");
            }
        }
    }
}
