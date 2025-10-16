/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryRankService
 *	@CreatedOn	: 10-16-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.service;

import dz.mdn.raas.common.administration.model.MilitaryRank;
import dz.mdn.raas.common.administration.model.MilitaryCategory;
import dz.mdn.raas.common.administration.repository.MilitaryRankRepository;
import dz.mdn.raas.common.administration.repository.MilitaryCategoryRepository;
import dz.mdn.raas.common.administration.dto.MilitaryRankDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Military Rank Service with CRUD operations
 * Handles military rank management operations with multilingual support and foreign key relationships
 * Based on exact field names: F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=abbreviationAr, F_05=abbreviationEn, F_06=abbreviationFr, F_07=militaryCategory
 * F_03 (designationFr) has unique constraint and is required
 * F_06 (abbreviationFr) is required
 * F_07 (militaryCategory) is required foreign key
 * F_01, F_02, F_04, F_05 are optional
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MilitaryRankService {

    private final MilitaryRankRepository militaryRankRepository;
    private final MilitaryCategoryRepository militaryCategoryRepository;

    // ========== CREATE OPERATIONS ==========

    /**
     * Create new military rank
     */
    public MilitaryRankDTO createMilitaryRank(MilitaryRankDTO militaryRankDTO) {
        log.info("Creating military rank with French designation: {} and designations: AR={}, EN={}, Abbreviations: FR={}, AR={}, EN={}, Category ID: {}", 
                militaryRankDTO.getDesignationFr(), militaryRankDTO.getDesignationAr(), 
                militaryRankDTO.getDesignationEn(), militaryRankDTO.getAbbreviationFr(),
                militaryRankDTO.getAbbreviationAr(), militaryRankDTO.getAbbreviationEn(),
                militaryRankDTO.getMilitaryCategoryId());

        // Validate required fields
        validateRequiredFields(militaryRankDTO, "create");

        // Check for unique constraint violation
        validateUniqueConstraints(militaryRankDTO, null);

        // Validate military category exists
        MilitaryCategory militaryCategory = validateAndGetMilitaryCategory(militaryRankDTO.getMilitaryCategoryId());

        // Create entity with exact field mapping
        MilitaryRank militaryRank = new MilitaryRank();
        militaryRank.setDesignationAr(militaryRankDTO.getDesignationAr()); // F_01
        militaryRank.setDesignationEn(militaryRankDTO.getDesignationEn()); // F_02
        militaryRank.setDesignationFr(militaryRankDTO.getDesignationFr()); // F_03
        militaryRank.setAbbreviationAr(militaryRankDTO.getAbbreviationAr()); // F_04
        militaryRank.setAbbreviationEn(militaryRankDTO.getAbbreviationEn()); // F_05
        militaryRank.setAbbreviationFr(militaryRankDTO.getAbbreviationFr()); // F_06
        militaryRank.setMilitaryCategory(militaryCategory); // F_07

        MilitaryRank savedMilitaryRank = militaryRankRepository.save(militaryRank);
        log.info("Successfully created military rank with ID: {}", savedMilitaryRank.getId());

        return MilitaryRankDTO.fromEntity(savedMilitaryRank);
    }

    // ========== READ OPERATIONS ==========

    /**
     * Get military rank by ID
     */
    @Transactional(readOnly = true)
    public MilitaryRankDTO getMilitaryRankById(Long id) {
        log.debug("Getting military rank with ID: {}", id);

        MilitaryRank militaryRank = militaryRankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Military rank not found with ID: " + id));

        return MilitaryRankDTO.fromEntity(militaryRank);
    }

    /**
     * Get military rank entity by ID
     */
    @Transactional(readOnly = true)
    public MilitaryRank getMilitaryRankEntityById(Long id) {
        return militaryRankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Military rank not found with ID: " + id));
    }

    /**
     * Find military rank by French designation (unique field F_03)
     */
    @Transactional(readOnly = true)
    public Optional<MilitaryRankDTO> findByDesignationFr(String designationFr) {
        log.debug("Finding military rank with French designation: {}", designationFr);

        return militaryRankRepository.findByDesignationFr(designationFr)
                .map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Find military rank by French abbreviation (F_06)
     */
    @Transactional(readOnly = true)
    public Optional<MilitaryRankDTO> findByAbbreviationFr(String abbreviationFr) {
        log.debug("Finding military rank with French abbreviation: {}", abbreviationFr);

        return militaryRankRepository.findByAbbreviationFr(abbreviationFr)
                .map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Find military rank by Arabic designation (F_01)
     */
    @Transactional(readOnly = true)
    public Optional<MilitaryRankDTO> findByDesignationAr(String designationAr) {
        log.debug("Finding military rank with Arabic designation: {}", designationAr);

        return militaryRankRepository.findByDesignationAr(designationAr)
                .map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Find military rank by English designation (F_02)
     */
    @Transactional(readOnly = true)
    public Optional<MilitaryRankDTO> findByDesignationEn(String designationEn) {
        log.debug("Finding military rank with English designation: {}", designationEn);

        return militaryRankRepository.findByDesignationEn(designationEn)
                .map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Find military ranks by military category ID (F_07)
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> findByMilitaryCategoryId(Long militaryCategoryId, Pageable pageable) {
        log.debug("Finding military ranks for military category ID: {}", militaryCategoryId);

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findByMilitaryCategoryId(militaryCategoryId, pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get all military ranks with pagination
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getAllMilitaryRanks(Pageable pageable) {
        log.debug("Getting all military ranks with pagination");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findAllOrderByDesignationFr(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get all military ranks ordered by military category and designation
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getAllMilitaryRanksOrderedByCategory(Pageable pageable) {
        log.debug("Getting all military ranks ordered by military category and designation");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findAllOrderByCategoryAndDesignation(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Find one military rank by ID
     */
    @Transactional(readOnly = true)
    public Optional<MilitaryRankDTO> findOne(Long id) {
        log.debug("Finding military rank by ID: {}", id);

        return militaryRankRepository.findById(id)
                .map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Search military ranks by designation or abbreviation
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> searchMilitaryRanks(String searchTerm, Pageable pageable) {
        log.debug("Searching military ranks with term: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllMilitaryRanks(pageable);
        }

        Page<MilitaryRank> militaryRanks = militaryRankRepository.searchByDesignationOrAbbreviation(searchTerm.trim(), pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Search military ranks with military category context
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> searchMilitaryRanksWithCategoryContext(String searchTerm, Pageable pageable) {
        log.debug("Searching military ranks with military category context for term: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllMilitaryRanks(pageable);
        }

        Page<MilitaryRank> militaryRanks = militaryRankRepository.searchWithMilitaryCategoryContext(searchTerm.trim(), pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get multilingual military ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getMultilingualMilitaryRanks(Pageable pageable) {
        log.debug("Getting multilingual military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findMultilingualMilitaryRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get officer ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getOfficerRanks(Pageable pageable) {
        log.debug("Getting officer military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findOfficerRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get senior officer ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getSeniorOfficerRanks(Pageable pageable) {
        log.debug("Getting senior officer military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findSeniorOfficerRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get NCO ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getNCORanks(Pageable pageable) {
        log.debug("Getting NCO military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findNCORanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get enlisted ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getEnlistedRanks(Pageable pageable) {
        log.debug("Getting enlisted military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findEnlistedRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get general ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getGeneralRanks(Pageable pageable) {
        log.debug("Getting general military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findGeneralRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get command ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getCommandRanks(Pageable pageable) {
        log.debug("Getting command military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findCommandRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Find military ranks by military category designation
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> findByMilitaryCategoryDesignation(String categoryDesignation, Pageable pageable) {
        log.debug("Finding military ranks by military category designation: {}", categoryDesignation);

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findByMilitaryCategoryDesignation(categoryDesignation, pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Find military ranks by military category code
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> findByMilitaryCategoryCode(String categoryCode, Pageable pageable) {
        log.debug("Finding military ranks by military category code: {}", categoryCode);

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findByMilitaryCategoryCode(categoryCode, pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get specific rank levels
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getTopRanks(Pageable pageable) {
        log.debug("Getting top military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findTopRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getColonelRanks(Pageable pageable) {
        log.debug("Getting colonel military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findColonelRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getMajorRanks(Pageable pageable) {
        log.debug("Getting major military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findMajorRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getCaptainRanks(Pageable pageable) {
        log.debug("Getting captain military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findCaptainRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getLieutenantRanks(Pageable pageable) {
        log.debug("Getting lieutenant military ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findLieutenantRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get military ranks missing translations
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getMilitaryRanksMissingTranslations(Pageable pageable) {
        log.debug("Getting military ranks missing translations");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findMissingTranslations(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    // ========== UPDATE OPERATIONS ==========

    /**
     * Update military rank
     */
    public MilitaryRankDTO updateMilitaryRank(Long id, MilitaryRankDTO militaryRankDTO) {
        log.info("Updating military rank with ID: {}", id);

        MilitaryRank existingMilitaryRank = getMilitaryRankEntityById(id);

        // Validate required fields
        validateRequiredFields(militaryRankDTO, "update");

        // Check for unique constraint violation (excluding current record)
        validateUniqueConstraints(militaryRankDTO, id);

        // Validate military category exists if being updated
        MilitaryCategory militaryCategory = null;
        if (militaryRankDTO.getMilitaryCategoryId() != null) {
            militaryCategory = validateAndGetMilitaryCategory(militaryRankDTO.getMilitaryCategoryId());
        }

        // Update fields with exact field mapping
        existingMilitaryRank.setDesignationAr(militaryRankDTO.getDesignationAr()); // F_01
        existingMilitaryRank.setDesignationEn(militaryRankDTO.getDesignationEn()); // F_02
        existingMilitaryRank.setDesignationFr(militaryRankDTO.getDesignationFr()); // F_03
        existingMilitaryRank.setAbbreviationAr(militaryRankDTO.getAbbreviationAr()); // F_04
        existingMilitaryRank.setAbbreviationEn(militaryRankDTO.getAbbreviationEn()); // F_05
        existingMilitaryRank.setAbbreviationFr(militaryRankDTO.getAbbreviationFr()); // F_06
        if (militaryCategory != null) {
            existingMilitaryRank.setMilitaryCategory(militaryCategory); // F_07
        }

        MilitaryRank updatedMilitaryRank = militaryRankRepository.save(existingMilitaryRank);
        log.info("Successfully updated military rank with ID: {}", id);

        return MilitaryRankDTO.fromEntity(updatedMilitaryRank);
    }

    // ========== DELETE OPERATIONS ==========

    /**
     * Delete military rank
     */
    public void deleteMilitaryRank(Long id) {
        log.info("Deleting military rank with ID: {}", id);

        MilitaryRank militaryRank = getMilitaryRankEntityById(id);
        militaryRankRepository.delete(militaryRank);

        log.info("Successfully deleted military rank with ID: {}", id);
    }

    /**
     * Delete military rank by ID (direct)
     */
    public void deleteMilitaryRankById(Long id) {
        log.info("Deleting military rank by ID: {}", id);

        if (!militaryRankRepository.existsById(id)) {
            throw new RuntimeException("Military rank not found with ID: " + id);
        }

        militaryRankRepository.deleteById(id);
        log.info("Successfully deleted military rank with ID: {}", id);
    }

    // ========== UTILITY METHODS ==========

    /**
     * Check if military rank exists
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return militaryRankRepository.existsById(id);
    }

    /**
     * Check if military rank exists by French designation
     */
    @Transactional(readOnly = true)
    public boolean existsByDesignationFr(String designationFr) {
        return militaryRankRepository.existsByDesignationFr(designationFr);
    }

    /**
     * Check if military rank exists by French abbreviation
     */
    @Transactional(readOnly = true)
    public boolean existsByAbbreviationFr(String abbreviationFr) {
        return militaryRankRepository.existsByAbbreviationFr(abbreviationFr);
    }

    /**
     * Get total count of military ranks
     */
    @Transactional(readOnly = true)
    public Long getTotalCount() {
        return militaryRankRepository.countAllMilitaryRanks();
    }

    /**
     * Get count by military category
     */
    @Transactional(readOnly = true)
    public Long getCountByMilitaryCategory(Long militaryCategoryId) {
        return militaryRankRepository.countByMilitaryCategoryId(militaryCategoryId);
    }

    /**
     * Get count of senior officer ranks
     */
    @Transactional(readOnly = true)
    public Long getSeniorOfficerRanksCount() {
        return militaryRankRepository.countSeniorOfficerRanks();
    }

    /**
     * Get count of officer ranks
     */
    @Transactional(readOnly = true)
    public Long getOfficerRanksCount() {
        return militaryRankRepository.countOfficerRanks();
    }

    /**
     * Get count of NCO ranks
     */
    @Transactional(readOnly = true)
    public Long getNCORanksCount() {
        return militaryRankRepository.countNCORanks();
    }

    /**
     * Get count of enlisted ranks
     */
    @Transactional(readOnly = true)
    public Long getEnlistedRanksCount() {
        return militaryRankRepository.countEnlistedRanks();
    }

    /**
     * Get count of multilingual military ranks
     */
    @Transactional(readOnly = true)
    public Long getMultilingualCount() {
        return militaryRankRepository.countMultilingualMilitaryRanks();
    }

    // ========== VALIDATION METHODS ==========

    /**
     * Validate required fields
     */
    private void validateRequiredFields(MilitaryRankDTO militaryRankDTO, String operation) {
        if (militaryRankDTO.getDesignationFr() == null || militaryRankDTO.getDesignationFr().trim().isEmpty()) {
            throw new RuntimeException("French designation is required for " + operation);
        }
        if (militaryRankDTO.getAbbreviationFr() == null || militaryRankDTO.getAbbreviationFr().trim().isEmpty()) {
            throw new RuntimeException("French abbreviation is required for " + operation);
        }
        if (militaryRankDTO.getMilitaryCategoryId() == null) {
            throw new RuntimeException("Military category is required for " + operation);
        }
    }

    /**
     * Validate unique constraints
     */
    private void validateUniqueConstraints(MilitaryRankDTO militaryRankDTO, Long excludeId) {
        // Check French designation uniqueness (F_03)
        if (excludeId == null) {
            if (militaryRankRepository.existsByDesignationFr(militaryRankDTO.getDesignationFr())) {
                throw new RuntimeException("Military rank with French designation '" + militaryRankDTO.getDesignationFr() + "' already exists");
            }
        } else {
            if (militaryRankRepository.existsByDesignationFrAndIdNot(militaryRankDTO.getDesignationFr(), excludeId)) {
                throw new RuntimeException("Another military rank with French designation '" + militaryRankDTO.getDesignationFr() + "' already exists");
            }
        }

        // Additional check for abbreviation uniqueness (business rule)
        if (militaryRankDTO.getAbbreviationFr() != null && !militaryRankDTO.getAbbreviationFr().trim().isEmpty()) {
            if (excludeId == null) {
                if (militaryRankRepository.existsByAbbreviationFr(militaryRankDTO.getAbbreviationFr())) {
                    throw new RuntimeException("Military rank with French abbreviation '" + militaryRankDTO.getAbbreviationFr() + "' already exists");
                }
            } else {
                if (militaryRankRepository.existsByAbbreviationFrAndIdNot(militaryRankDTO.getAbbreviationFr(), excludeId)) {
                    throw new RuntimeException("Another military rank with French abbreviation '" + militaryRankDTO.getAbbreviationFr() + "' already exists");
                }
            }
        }
    }

    /**
     * Validate and get military category
     */
    private MilitaryCategory validateAndGetMilitaryCategory(Long militaryCategoryId) {
        return militaryCategoryRepository.findById(militaryCategoryId)
                .orElseThrow(() -> new RuntimeException("Military category not found with ID: " + militaryCategoryId));
    }
}
