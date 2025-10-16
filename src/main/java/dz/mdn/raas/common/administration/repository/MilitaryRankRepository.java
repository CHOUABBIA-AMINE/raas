/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryRankRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.MilitaryRank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MilitaryRank Repository with essential CRUD operations
 * Based on exact field names: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=militaryCategory
 * F_03 (designationFr) has unique constraint and is required
 * F_01 (designationAr) and F_02 (designationEn) are optional
 * F_04 (militaryCategory) is required foreign key
 */
@Repository
public interface MilitaryRankRepository extends JpaRepository<MilitaryRank, Long> {

    /**
     * Find military rank by French designation (F_03) - unique field
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.designationFr = :designationFr")
    Optional<MilitaryRank> findByDesignationFr(@Param("designationFr") String designationFr);

    /**
     * Find military rank by Arabic designation (F_01)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.designationAr = :designationAr")
    Optional<MilitaryRank> findByDesignationAr(@Param("designationAr") String designationAr);

    /**
     * Find military rank by English designation (F_02)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.designationEn = :designationEn")
    Optional<MilitaryRank> findByDesignationEn(@Param("designationEn") String designationEn);

    /**
     * Check if military rank exists by French designation
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM MilitaryRank r WHERE r.designationFr = :designationFr")
    boolean existsByDesignationFr(@Param("designationFr") String designationFr);

    /**
     * Check unique constraint for updates (excluding current ID)
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM MilitaryRank r WHERE r.designationFr = :designationFr AND r.id != :id")
    boolean existsByDesignationFrAndIdNot(@Param("designationFr") String designationFr, @Param("id") Long id);

    /**
     * Find military ranks by military category ID (F_04)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.militaryCategory.id = :categoryId ORDER BY r.designationFr ASC")
    Page<MilitaryRank> findByMilitaryCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Find all military ranks with pagination ordered by French designation
     */
    @Query("SELECT r FROM MilitaryRank r ORDER BY r.designationFr ASC")
    Page<MilitaryRank> findAllOrderByDesignationFr(Pageable pageable);

    /**
     * Find all military ranks ordered by military category and designation
     */
    @Query("SELECT r FROM MilitaryRank r ORDER BY r.militaryCategory.designationFr ASC, r.designationFr ASC")
    Page<MilitaryRank> findAllOrderByCategoryAndDesignation(Pageable pageable);

    /**
     * Search military ranks by any designation field
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "r.designationAr LIKE %:search% OR " +
           "r.designationEn LIKE %:search% OR " +
           "r.designationFr LIKE %:search%")
    Page<MilitaryRank> searchByDesignation(@Param("search") String search, Pageable pageable);

    /**
     * Find military ranks by French designation pattern (F_03)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.designationFr LIKE %:pattern%")
    Page<MilitaryRank> findByDesignationFrContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find military ranks by Arabic designation pattern (F_01)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.designationAr LIKE %:pattern%")
    Page<MilitaryRank> findByDesignationArContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find military ranks by English designation pattern (F_02)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.designationEn LIKE %:pattern%")
    Page<MilitaryRank> findByDesignationEnContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Count total military ranks
     */
    @Query("SELECT COUNT(r) FROM MilitaryRank r")
    Long countAllMilitaryRanks();

    /**
     * Count military ranks by category
     */
    @Query("SELECT COUNT(r) FROM MilitaryRank r WHERE r.militaryCategory.id = :categoryId")
    Long countByMilitaryCategoryId(@Param("categoryId") Long categoryId);

    /**
     * Find military ranks that have Arabic designation
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.designationAr IS NOT NULL AND r.designationAr != ''")
    Page<MilitaryRank> findWithArabicDesignation(Pageable pageable);

    /**
     * Find military ranks that have English designation
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.designationEn IS NOT NULL AND r.designationEn != ''")
    Page<MilitaryRank> findWithEnglishDesignation(Pageable pageable);

    /**
     * Find multilingual military ranks (have at least 2 designations)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "(r.designationAr IS NOT NULL AND r.designationAr != '' AND r.designationEn IS NOT NULL AND r.designationEn != '') OR " +
           "(r.designationAr IS NOT NULL AND r.designationAr != '' AND r.designationFr IS NOT NULL AND r.designationFr != '') OR " +
           "(r.designationEn IS NOT NULL AND r.designationEn != '' AND r.designationFr IS NOT NULL AND r.designationFr != '')")
    Page<MilitaryRank> findMultilingualMilitaryRanks(Pageable pageable);

    /**
     * Find general officer ranks (based on French designation patterns)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%général%' OR LOWER(r.designationFr) LIKE '%amiral%' OR " +
           "LOWER(r.designationFr) LIKE '%general%' OR LOWER(r.designationFr) LIKE '%admiral%'")
    Page<MilitaryRank> findGeneralOfficerRanks(Pageable pageable);

    /**
     * Find field officer ranks (based on French designation patterns)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%colonel%' OR LOWER(r.designationFr) LIKE '%lieutenant-colonel%' OR " +
           "LOWER(r.designationFr) LIKE '%commandant%' OR LOWER(r.designationFr) LIKE '%major%'")
    Page<MilitaryRank> findFieldOfficerRanks(Pageable pageable);

    /**
     * Find company officer ranks (based on French designation patterns)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%capitaine%' OR LOWER(r.designationFr) LIKE '%lieutenant%' OR " +
           "LOWER(r.designationFr) LIKE '%sous-lieutenant%' OR LOWER(r.designationFr) LIKE '%enseigne%'")
    Page<MilitaryRank> findCompanyOfficerRanks(Pageable pageable);

    /**
     * Find senior NCO ranks (based on French designation patterns)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%adjudant-chef%' OR LOWER(r.designationFr) LIKE '%adjudant%' OR " +
           "LOWER(r.designationFr) LIKE '%sergent-chef%' OR LOWER(r.designationFr) LIKE '%maître%'")
    Page<MilitaryRank> findSeniorNCORanks(Pageable pageable);

    /**
     * Find junior NCO ranks (based on French designation patterns)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%sergent%' OR LOWER(r.designationFr) LIKE '%caporal-chef%' OR " +
           "LOWER(r.designationFr) LIKE '%quartier-maître%'")
    Page<MilitaryRank> findJuniorNCORanks(Pageable pageable);

    /**
     * Find enlisted ranks (based on French designation patterns)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%caporal%' OR LOWER(r.designationFr) LIKE '%soldat%' OR " +
           "LOWER(r.designationFr) LIKE '%matelot%' OR LOWER(r.designationFr) LIKE '%aviateur%'")
    Page<MilitaryRank> findEnlistedRanks(Pageable pageable);

    /**
     * Find cadet ranks (based on French designation patterns)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%élève%' OR LOWER(r.designationFr) LIKE '%aspirant%' OR " +
           "LOWER(r.designationFr) LIKE '%cadet%'")
    Page<MilitaryRank> findCadetRanks(Pageable pageable);

    /**
     * Find officer ranks (all officer levels)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%général%' OR LOWER(r.designationFr) LIKE '%amiral%' OR " +
           "LOWER(r.designationFr) LIKE '%colonel%' OR LOWER(r.designationFr) LIKE '%commandant%' OR " +
           "LOWER(r.designationFr) LIKE '%capitaine%' OR LOWER(r.designationFr) LIKE '%lieutenant%'")
    Page<MilitaryRank> findOfficerRanks(Pageable pageable);

    /**
     * Find NCO ranks (all NCO levels)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%adjudant%' OR LOWER(r.designationFr) LIKE '%sergent%' OR " +
           "LOWER(r.designationFr) LIKE '%caporal-chef%' OR LOWER(r.designationFr) LIKE '%maître%'")
    Page<MilitaryRank> findNCORanks(Pageable pageable);

    /**
     * Find ranks requiring security clearance (officers and senior NCOs)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%général%' OR LOWER(r.designationFr) LIKE '%amiral%' OR " +
           "LOWER(r.designationFr) LIKE '%colonel%' OR LOWER(r.designationFr) LIKE '%commandant%' OR " +
           "LOWER(r.designationFr) LIKE '%capitaine%' OR LOWER(r.designationFr) LIKE '%lieutenant%' OR " +
           "LOWER(r.designationFr) LIKE '%adjudant%'")
    Page<MilitaryRank> findSecurityClearanceRanks(Pageable pageable);

    /**
     * Find promotion eligible ranks (excluding highest ranks)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "NOT (LOWER(r.designationFr) LIKE '%général d''armée%' OR LOWER(r.designationFr) LIKE '%amiral%')")
    Page<MilitaryRank> findPromotionEligibleRanks(Pageable pageable);

    /**
     * Find military ranks ordered by designation in specific language
     */
    @Query("SELECT r FROM MilitaryRank r ORDER BY r.designationAr ASC")
    Page<MilitaryRank> findAllOrderByDesignationAr(Pageable pageable);

    @Query("SELECT r FROM MilitaryRank r ORDER BY r.designationEn ASC")
    Page<MilitaryRank> findAllOrderByDesignationEn(Pageable pageable);

    /**
     * Count ranks by level
     */
    @Query("SELECT COUNT(r) FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%général%' OR LOWER(r.designationFr) LIKE '%amiral%'")
    Long countGeneralOfficerRanks();

    @Query("SELECT COUNT(r) FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%colonel%' OR LOWER(r.designationFr) LIKE '%commandant%'")
    Long countFieldOfficerRanks();

    @Query("SELECT COUNT(r) FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%capitaine%' OR LOWER(r.designationFr) LIKE '%lieutenant%'")
    Long countCompanyOfficerRanks();

    @Query("SELECT COUNT(r) FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%adjudant%' OR LOWER(r.designationFr) LIKE '%sergent%'")
    Long countNCORanks();

    @Query("SELECT COUNT(r) FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%caporal%' OR LOWER(r.designationFr) LIKE '%soldat%'")
    Long countEnlistedRanks();

    /**
     * Find ranks by service branch (based on designation patterns)
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%terre%' OR LOWER(r.designationFr) LIKE '%armée%'")
    Page<MilitaryRank> findArmyRanks(Pageable pageable);

    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%marine%' OR LOWER(r.designationFr) LIKE '%naval%' OR " +
           "LOWER(r.designationFr) LIKE '%matelot%' OR LOWER(r.designationFr) LIKE '%amiral%'")
    Page<MilitaryRank> findNavyRanks(Pageable pageable);

    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%air%' OR LOWER(r.designationFr) LIKE '%aviateur%' OR " +
           "LOWER(r.designationFr) LIKE '%aviation%'")
    Page<MilitaryRank> findAirForceRanks(Pageable pageable);

    /**
     * Find ranks by command level
     */
    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%général%' OR LOWER(r.designationFr) LIKE '%amiral%'")
    Page<MilitaryRank> findStrategicCommandRanks(Pageable pageable);

    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%colonel%' OR LOWER(r.designationFr) LIKE '%commandant%'")
    Page<MilitaryRank> findOperationalCommandRanks(Pageable pageable);

    @Query("SELECT r FROM MilitaryRank r WHERE " +
           "LOWER(r.designationFr) LIKE '%capitaine%' OR LOWER(r.designationFr) LIKE '%lieutenant%'")
    Page<MilitaryRank> findTacticalCommandRanks(Pageable pageable);

    /**
     * Search ranks by rank level pattern
     */
    @Query("SELECT r FROM MilitaryRank r WHERE LOWER(r.designationFr) LIKE %:levelPattern%")
    Page<MilitaryRank> findByRankLevel(@Param("levelPattern") String levelPattern, Pageable pageable);

    /**
     * Find ranks with join fetch for military category
     */
    @Query("SELECT r FROM MilitaryRank r JOIN FETCH r.militaryCategory ORDER BY r.designationFr ASC")
    Page<MilitaryRank> findAllWithCategory(Pageable pageable);

    /**
     * Find ranks by category designation
     */
    @Query("SELECT r FROM MilitaryRank r WHERE r.militaryCategory.designationFr = :categoryDesignation ORDER BY r.designationFr ASC")
    Page<MilitaryRank> findByCategoryDesignation(@Param("categoryDesignation") String categoryDesignation, Pageable pageable);
}
