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
 * Military Rank Repository with essential CRUD operations
 * Based on exact field names: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=abbreviationAr, F_05=abbreviationEn, F_06=abbreviationFr, F_07=militaryCategory
 * F_03 (designationFr) has unique constraint and is required
 * F_06 (abbreviationFr) is required
 * F_07 (militaryCategory) is required foreign key
 * F_01, F_02, F_04, F_05 are optional
 */
@Repository
public interface MilitaryRankRepository extends JpaRepository<MilitaryRank, Long> {

    /**
     * Find military rank by French designation (F_03) - unique field
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.designationFr = :designationFr")
    Optional<MilitaryRank> findByDesignationFr(@Param("designationFr") String designationFr);

    /**
     * Find military rank by French abbreviation (F_06)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.abbreviationFr = :abbreviationFr")
    Optional<MilitaryRank> findByAbbreviationFr(@Param("abbreviationFr") String abbreviationFr);

    /**
     * Find military ranks by military category ID (F_07)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.militaryCategory.id = :militaryCategoryId ORDER BY mr.designationFr ASC")
    Page<MilitaryRank> findByMilitaryCategoryId(@Param("militaryCategoryId") Long militaryCategoryId, Pageable pageable);

    /**
     * Check if military rank exists by French designation
     */
    @Query("SELECT CASE WHEN COUNT(mr) > 0 THEN true ELSE false END FROM MilitaryRank mr WHERE mr.designationFr = :designationFr")
    boolean existsByDesignationFr(@Param("designationFr") String designationFr);

    /**
     * Check unique constraint for updates (excluding current ID)
     */
    @Query("SELECT CASE WHEN COUNT(mr) > 0 THEN true ELSE false END FROM MilitaryRank mr WHERE mr.designationFr = :designationFr AND mr.id != :id")
    boolean existsByDesignationFrAndIdNot(@Param("designationFr") String designationFr, @Param("id") Long id);

    /**
     * Find all military ranks with pagination ordered by French designation
     */
    @Query("SELECT mr FROM MilitaryRank mr ORDER BY mr.designationFr ASC")
    Page<MilitaryRank> findAllOrderByDesignationFr(Pageable pageable);

    /**
     * Find all military ranks ordered by military category and designation
     */
    @Query("SELECT mr FROM MilitaryRank mr ORDER BY mr.militaryCategory.designationFr ASC, mr.designationFr ASC")
    Page<MilitaryRank> findAllOrderByCategoryAndDesignation(Pageable pageable);

    /**
     * Search military ranks by any designation or abbreviation field
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "mr.designationAr LIKE %:search% OR " +
           "mr.designationEn LIKE %:search% OR " +
           "mr.designationFr LIKE %:search% OR " +
           "mr.abbreviationAr LIKE %:search% OR " +
           "mr.abbreviationEn LIKE %:search% OR " +
           "mr.abbreviationFr LIKE %:search%")
    Page<MilitaryRank> searchByDesignationOrAbbreviation(@Param("search") String search, Pageable pageable);

    /**
     * Find military ranks by French designation pattern (F_03)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.designationFr LIKE %:pattern%")
    Page<MilitaryRank> findByDesignationFrContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find military ranks by French abbreviation pattern (F_06)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.abbreviationFr LIKE %:pattern%")
    Page<MilitaryRank> findByAbbreviationFrContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Count total military ranks
     */
    @Query("SELECT COUNT(mr) FROM MilitaryRank mr")
    Long countAllMilitaryRanks();

    /**
     * Count military ranks by military category
     */
    @Query("SELECT COUNT(mr) FROM MilitaryRank mr WHERE mr.militaryCategory.id = :militaryCategoryId")
    Long countByMilitaryCategoryId(@Param("militaryCategoryId") Long militaryCategoryId);

    /**
     * Find military ranks that have Arabic designation
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.designationAr IS NOT NULL AND mr.designationAr != ''")
    Page<MilitaryRank> findWithArabicDesignation(Pageable pageable);

    /**
     * Find military ranks that have English designation
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.designationEn IS NOT NULL AND mr.designationEn != ''")
    Page<MilitaryRank> findWithEnglishDesignation(Pageable pageable);

    /**
     * Find multilingual military ranks (have at least 2 designations)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "(mr.designationAr IS NOT NULL AND mr.designationAr != '' AND mr.designationEn IS NOT NULL AND mr.designationEn != '') OR " +
           "(mr.designationAr IS NOT NULL AND mr.designationAr != '' AND mr.designationFr IS NOT NULL AND mr.designationFr != '') OR " +
           "(mr.designationEn IS NOT NULL AND mr.designationEn != '' AND mr.designationFr IS NOT NULL AND mr.designationFr != '')")
    Page<MilitaryRank> findMultilingualMilitaryRanks(Pageable pageable);

    /**
     * Find officer ranks (based on French designation patterns)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%général%' OR LOWER(mr.designationFr) LIKE '%colonel%' OR " +
           "LOWER(mr.designationFr) LIKE '%commandant%' OR LOWER(mr.designationFr) LIKE '%capitaine%' OR " +
           "LOWER(mr.designationFr) LIKE '%lieutenant%' OR LOWER(mr.designationFr) LIKE '%major%'")
    Page<MilitaryRank> findOfficerRanks(Pageable pageable);

    /**
     * Find senior officer ranks (based on French designation patterns)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%général%' OR LOWER(mr.designationFr) LIKE '%amiral%' OR " +
           "LOWER(mr.designationFr) LIKE '%colonel%'")
    Page<MilitaryRank> findSeniorOfficerRanks(Pageable pageable);

    /**
     * Find NCO ranks (based on French designation patterns)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%adjudant%' OR LOWER(mr.designationFr) LIKE '%sergent%'")
    Page<MilitaryRank> findNCORanks(Pageable pageable);

    /**
     * Find enlisted ranks (based on French designation patterns)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%caporal%' OR LOWER(mr.designationFr) LIKE '%soldat%'")
    Page<MilitaryRank> findEnlistedRanks(Pageable pageable);

    /**
     * Find general ranks (based on French designation patterns)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%général%' OR LOWER(mr.designationFr) LIKE '%amiral%'")
    Page<MilitaryRank> findGeneralRanks(Pageable pageable);

    /**
     * Find command ranks (leadership positions)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%général%' OR LOWER(mr.designationFr) LIKE '%colonel%' OR " +
           "LOWER(mr.designationFr) LIKE '%commandant%' OR LOWER(mr.designationFr) LIKE '%capitaine%' OR " +
           "LOWER(mr.designationFr) LIKE '%adjudant%'")
    Page<MilitaryRank> findCommandRanks(Pageable pageable);

    /**
     * Find military ranks with join fetch for military category
     */
    @Query("SELECT mr FROM MilitaryRank mr JOIN FETCH mr.militaryCategory ORDER BY mr.designationFr ASC")
    Page<MilitaryRank> findAllWithMilitaryCategory(Pageable pageable);

    /**
     * Find military ranks by military category designation
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.militaryCategory.designationFr = :categoryDesignation ORDER BY mr.designationFr ASC")
    Page<MilitaryRank> findByMilitaryCategoryDesignation(@Param("categoryDesignation") String categoryDesignation, Pageable pageable);

    /**
     * Find military ranks by military category code
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.militaryCategory.code = :categoryCode ORDER BY mr.designationFr ASC")
    Page<MilitaryRank> findByMilitaryCategoryCode(@Param("categoryCode") String categoryCode, Pageable pageable);

    /**
     * Search military ranks with military category context
     */
    @Query("SELECT mr FROM MilitaryRank mr LEFT JOIN mr.militaryCategory mc WHERE " +
           "(mr.designationAr LIKE %:search% OR mr.designationEn LIKE %:search% OR " +
           "mr.designationFr LIKE %:search% OR mr.abbreviationAr LIKE %:search% OR " +
           "mr.abbreviationEn LIKE %:search% OR mr.abbreviationFr LIKE %:search% OR " +
           "mc.designationFr LIKE %:search% OR mc.code LIKE %:search%) " +
           "ORDER BY mr.designationFr ASC")
    Page<MilitaryRank> searchWithMilitaryCategoryContext(@Param("search") String search, Pageable pageable);

    /**
     * Find military ranks ordered by designation in specific language
     */
    @Query("SELECT mr FROM MilitaryRank mr ORDER BY mr.designationAr ASC")
    Page<MilitaryRank> findAllOrderByDesignationAr(Pageable pageable);

    @Query("SELECT mr FROM MilitaryRank mr ORDER BY mr.designationEn ASC")
    Page<MilitaryRank> findAllOrderByDesignationEn(Pageable pageable);

    /**
     * Count military ranks by category
     */
    @Query("SELECT COUNT(mr) FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%général%' OR LOWER(mr.designationFr) LIKE '%amiral%' OR " +
           "LOWER(mr.designationFr) LIKE '%colonel%'")
    Long countSeniorOfficerRanks();

    @Query("SELECT COUNT(mr) FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%commandant%' OR LOWER(mr.designationFr) LIKE '%capitaine%' OR " +
           "LOWER(mr.designationFr) LIKE '%lieutenant%' OR LOWER(mr.designationFr) LIKE '%major%'")
    Long countOfficerRanks();

    @Query("SELECT COUNT(mr) FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%adjudant%' OR LOWER(mr.designationFr) LIKE '%sergent%'")
    Long countNCORanks();

    @Query("SELECT COUNT(mr) FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%caporal%' OR LOWER(mr.designationFr) LIKE '%soldat%'")
    Long countEnlistedRanks();

    /**
     * Count multilingual military ranks
     */
    @Query("SELECT COUNT(mr) FROM MilitaryRank mr WHERE " +
           "(mr.designationAr IS NOT NULL AND mr.designationAr != '' AND mr.designationEn IS NOT NULL AND mr.designationEn != '') OR " +
           "(mr.designationAr IS NOT NULL AND mr.designationAr != '' AND mr.designationFr IS NOT NULL AND mr.designationFr != '') OR " +
           "(mr.designationEn IS NOT NULL AND mr.designationEn != '' AND mr.designationFr IS NOT NULL AND mr.designationFr != '')")
    Long countMultilingualMilitaryRanks();

    /**
     * Find military ranks by rank hierarchy level (estimated based on keywords)
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%général major%' OR LOWER(mr.designationFr) LIKE '%amiral%'")
    Page<MilitaryRank> findTopRanks(Pageable pageable);

    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%général%' AND LOWER(mr.designationFr) NOT LIKE '%major%'")
    Page<MilitaryRank> findGeneralRanksExcludingTop(Pageable pageable);

    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%colonel%'")
    Page<MilitaryRank> findColonelRanks(Pageable pageable);

    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%commandant%' OR LOWER(mr.designationFr) LIKE '%major%'")
    Page<MilitaryRank> findMajorRanks(Pageable pageable);

    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%capitaine%'")
    Page<MilitaryRank> findCaptainRanks(Pageable pageable);

    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%lieutenant%' AND LOWER(mr.designationFr) NOT LIKE '%sous%'")
    Page<MilitaryRank> findLieutenantRanks(Pageable pageable);

    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "LOWER(mr.designationFr) LIKE '%sous-lieutenant%'")
    Page<MilitaryRank> findSubLieutenantRanks(Pageable pageable);

    /**
     * Find military ranks by abbreviation length
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE LENGTH(mr.abbreviationFr) <= :maxLength")
    Page<MilitaryRank> findByAbbreviationLength(@Param("maxLength") Integer maxLength, Pageable pageable);

    /**
     * Find military ranks missing translations
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE " +
           "(mr.designationAr IS NULL OR mr.designationAr = '') OR " +
           "(mr.designationEn IS NULL OR mr.designationEn = '') OR " +
           "(mr.abbreviationAr IS NULL OR mr.abbreviationAr = '') OR " +
           "(mr.abbreviationEn IS NULL OR mr.abbreviationEn = '')")
    Page<MilitaryRank> findMissingTranslations(Pageable pageable);

    /**
     * Check if abbreviation exists
     */
    @Query("SELECT CASE WHEN COUNT(mr) > 0 THEN true ELSE false END FROM MilitaryRank mr WHERE mr.abbreviationFr = :abbreviationFr")
    boolean existsByAbbreviationFr(@Param("abbreviationFr") String abbreviationFr);

    /**
     * Check if abbreviation exists excluding current ID
     */
    @Query("SELECT CASE WHEN COUNT(mr) > 0 THEN true ELSE false END FROM MilitaryRank mr WHERE mr.abbreviationFr = :abbreviationFr AND mr.id != :id")
    boolean existsByAbbreviationFrAndIdNot(@Param("abbreviationFr") String abbreviationFr, @Param("id") Long id);

    /**
     * Find military ranks by Arabic designation
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.designationAr = :designationAr")
    Optional<MilitaryRank> findByDesignationAr(@Param("designationAr") String designationAr);

    /**
     * Find military ranks by English designation
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.designationEn = :designationEn")
    Optional<MilitaryRank> findByDesignationEn(@Param("designationEn") String designationEn);
}