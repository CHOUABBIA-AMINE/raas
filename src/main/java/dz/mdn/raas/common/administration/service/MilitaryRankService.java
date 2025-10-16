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
 * MilitaryRank Service with CRUD operations
 * Handles military rank management operations with multilingual support and foreign key relationships
 * Based on exact field names: F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=militaryCategory
 * F_03 (designationFr) has unique constraint and is required
 * F_01 (designationAr) and F_02 (designationEn) are optional
 * F_04 (militaryCategory) is required foreign key
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
        log.info("Creating military rank with French designation: {} and designations: AR={}, EN={}, Category ID: {}", 
                militaryRankDTO.getDesignationFr(), militaryRankDTO.getDesignationAr(), 
                militaryRankDTO.getDesignationEn(), militaryRankDTO.getMilitaryCategoryId());

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
        militaryRank.setMilitaryCategory(militaryCategory); // F_04

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
     * Find military ranks by military category ID (F_04)
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> findByMilitaryCategoryId(Long categoryId, Pageable pageable) {
        log.debug("Finding military ranks for category ID: {}", categoryId);

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findByMilitaryCategoryId(categoryId, pageable);
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
     * Get all military ranks ordered by category and designation
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getAllMilitaryRanksOrderedByCategory(Pageable pageable) {
        log.debug("Getting all military ranks ordered by category and designation");

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
     * Search military ranks by designation
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> searchMilitaryRanks(String searchTerm, Pageable pageable) {
        log.debug("Searching military ranks with term: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllMilitaryRanks(pageable);
        }

        Page<MilitaryRank> militaryRanks = militaryRankRepository.searchByDesignation(searchTerm.trim(), pageable);
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
     * Get general officer ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getGeneralOfficerRanks(Pageable pageable) {
        log.debug("Getting general officer ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findGeneralOfficerRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get field officer ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getFieldOfficerRanks(Pageable pageable) {
        log.debug("Getting field officer ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findFieldOfficerRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get company officer ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getCompanyOfficerRanks(Pageable pageable) {
        log.debug("Getting company officer ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findCompanyOfficerRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get senior NCO ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getSeniorNCORanks(Pageable pageable) {
        log.debug("Getting senior NCO ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findSeniorNCORanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get junior NCO ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getJuniorNCORanks(Pageable pageable) {
        log.debug("Getting junior NCO ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findJuniorNCORanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get enlisted ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getEnlistedRanks(Pageable pageable) {
        log.debug("Getting enlisted ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findEnlistedRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get cadet ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getCadetRanks(Pageable pageable) {
        log.debug("Getting cadet ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findCadetRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get all officer ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getOfficerRanks(Pageable pageable) {
        log.debug("Getting officer ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findOfficerRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get all NCO ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getNCORanks(Pageable pageable) {
        log.debug("Getting NCO ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findNCORanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get security clearance ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getSecurityClearanceRanks(Pageable pageable) {
        log.debug("Getting security clearance ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findSecurityClearanceRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get promotion eligible ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getPromotionEligibleRanks(Pageable pageable) {
        log.debug("Getting promotion eligible ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findPromotionEligibleRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get Army ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getArmyRanks(Pageable pageable) {
        log.debug("Getting Army ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findArmyRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get Navy ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getNavyRanks(Pageable pageable) {
        log.debug("Getting Navy ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findNavyRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get Air Force ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getAirForceRanks(Pageable pageable) {
        log.debug("Getting Air Force ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findAirForceRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get strategic command ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getStrategicCommandRanks(Pageable pageable) {
        log.debug("Getting strategic command ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findStrategicCommandRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get operational command ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getOperationalCommandRanks(Pageable pageable) {
        log.debug("Getting operational command ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findOperationalCommandRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Get tactical command ranks
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> getTacticalCommandRanks(Pageable pageable) {
        log.debug("Getting tactical command ranks");

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findTacticalCommandRanks(pageable);
        return militaryRanks.map(MilitaryRankDTO::fromEntity);
    }

    /**
     * Find by category designation
     */
    @Transactional(readOnly = true)
    public Page<MilitaryRankDTO> findByCategoryDesignation(String categoryDesignation, Pageable pageable) {
        log.debug("Finding military ranks by category designation: {}", categoryDesignation);

        Page<MilitaryRank> militaryRanks = militaryRankRepository.findByCategoryDesignation(categoryDesignation, pageable);
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
        if (militaryCategory != null) {
            existingMilitaryRank.setMilitaryCategory(militaryCategory); // F_04
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
    public Long getCountByCategory(Long categoryId) {
        return militaryRankRepository.countByMilitaryCategoryId(categoryId);
    }

    /**
     * Get count of general officer ranks
     */
    @Transactional(readOnly = true)
    public Long getGeneralOfficerCount() {
        return militaryRankRepository.countGeneralOfficerRanks();
    }

    /**
     * Get count of field officer ranks
     */
    @Transactional(readOnly = true)
    public Long getFieldOfficerCount() {
        return militaryRankRepository.countFieldOfficerRanks();
    }

    /**
     * Get count of company officer ranks
     */
    @Transactional(readOnly = true)
    public Long getCompanyOfficerCount() {
        return militaryRankRepository.countCompanyOfficerRanks();
    }

    /**
     * Get count of NCO ranks
     */
    @Transactional(readOnly = true)
    public Long getNCOCount() {
        return militaryRankRepository.countNCORanks();
    }

    /**
     * Get count of enlisted ranks
     */
    @Transactional(readOnly = true)
    public Long getEnlistedCount() {
        return militaryRankRepository.countEnlistedRanks();
    }

    // ========== VALIDATION METHODS ==========

    /**
     * Validate required fields
     */
    private void validateRequiredFields(MilitaryRankDTO militaryRankDTO, String operation) {
        if (militaryRankDTO.getDesignationFr() == null || militaryRankDTO.getDesignationFr().trim().isEmpty()) {
            throw new RuntimeException("French designation is required for " + operation);
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
    }

    /**
     * Validate and get military category
     */
    private MilitaryCategory validateAndGetMilitaryCategory(Long categoryId) {
        return militaryCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Military category not found with ID: " + categoryId));
    }
}
