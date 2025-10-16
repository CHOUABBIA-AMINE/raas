/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryCategoryRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.MilitaryCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MilitaryCategory Repository with essential CRUD operations
 * Based on exact field names: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr
 * F_03 (designationFr) has unique constraint and is required
 * F_01 (designationAr) and F_02 (designationEn) are optional
 */
@Repository
public interface MilitaryCategoryRepository extends JpaRepository<MilitaryCategory, Long> {

    /**
     * Find military category by French designation (F_03) - unique field
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE m.designationFr = :designationFr")
    Optional<MilitaryCategory> findByDesignationFr(@Param("designationFr") String designationFr);

    /**
     * Find military category by Arabic designation (F_01)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE m.designationAr = :designationAr")
    Optional<MilitaryCategory> findByDesignationAr(@Param("designationAr") String designationAr);

    /**
     * Find military category by English designation (F_02)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE m.designationEn = :designationEn")
    Optional<MilitaryCategory> findByDesignationEn(@Param("designationEn") String designationEn);

    /**
     * Check if military category exists by French designation
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MilitaryCategory m WHERE m.designationFr = :designationFr")
    boolean existsByDesignationFr(@Param("designationFr") String designationFr);

    /**
     * Check unique constraint for updates (excluding current ID)
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MilitaryCategory m WHERE m.designationFr = :designationFr AND m.id != :id")
    boolean existsByDesignationFrAndIdNot(@Param("designationFr") String designationFr, @Param("id") Long id);

    /**
     * Find all military categories with pagination ordered by French designation
     */
    @Query("SELECT m FROM MilitaryCategory m ORDER BY m.designationFr ASC")
    Page<MilitaryCategory> findAllOrderByDesignationFr(Pageable pageable);

    /**
     * Search military categories by any designation field
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "m.designationAr LIKE %:search% OR " +
           "m.designationEn LIKE %:search% OR " +
           "m.designationFr LIKE %:search%")
    Page<MilitaryCategory> searchByDesignation(@Param("search") String search, Pageable pageable);

    /**
     * Find military categories by French designation pattern (F_03)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE m.designationFr LIKE %:pattern%")
    Page<MilitaryCategory> findByDesignationFrContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find military categories by Arabic designation pattern (F_01)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE m.designationAr LIKE %:pattern%")
    Page<MilitaryCategory> findByDesignationArContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find military categories by English designation pattern (F_02)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE m.designationEn LIKE %:pattern%")
    Page<MilitaryCategory> findByDesignationEnContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Count total military categories
     */
    @Query("SELECT COUNT(m) FROM MilitaryCategory m")
    Long countAllMilitaryCategories();

    /**
     * Find military categories that have Arabic designation
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE m.designationAr IS NOT NULL AND m.designationAr != ''")
    Page<MilitaryCategory> findWithArabicDesignation(Pageable pageable);

    /**
     * Find military categories that have English designation
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE m.designationEn IS NOT NULL AND m.designationEn != ''")
    Page<MilitaryCategory> findWithEnglishDesignation(Pageable pageable);

    /**
     * Find multilingual military categories (have at least 2 designations)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "(m.designationAr IS NOT NULL AND m.designationAr != '' AND m.designationEn IS NOT NULL AND m.designationEn != '') OR " +
           "(m.designationAr IS NOT NULL AND m.designationAr != '' AND m.designationFr IS NOT NULL AND m.designationFr != '') OR " +
           "(m.designationEn IS NOT NULL AND m.designationEn != '' AND m.designationFr IS NOT NULL AND m.designationFr != '')")
    Page<MilitaryCategory> findMultilingualMilitaryCategories(Pageable pageable);

    /**
     * Find officer categories (based on French designation patterns)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%officier%' OR LOWER(m.designationFr) LIKE '%officer%' OR " +
           "LOWER(m.designationFr) LIKE '%commandant%' OR LOWER(m.designationFr) LIKE '%colonel%'")
    Page<MilitaryCategory> findOfficerCategories(Pageable pageable);

    /**
     * Find NCO categories (based on French designation patterns)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%sous-officier%' OR LOWER(m.designationFr) LIKE '%sergent%' OR " +
           "LOWER(m.designationFr) LIKE '%adjudant%' OR LOWER(m.designationFr) LIKE '%nco%'")
    Page<MilitaryCategory> findNCOCategories(Pageable pageable);

    /**
     * Find enlisted categories (based on French designation patterns)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%soldat%' OR LOWER(m.designationFr) LIKE '%caporal%' OR " +
           "LOWER(m.designationFr) LIKE '%enlisted%' OR LOWER(m.designationFr) LIKE '%militaire du rang%'")
    Page<MilitaryCategory> findEnlistedCategories(Pageable pageable);

    /**
     * Find specialist categories (based on French designation patterns)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%spécialisé%' OR LOWER(m.designationFr) LIKE '%specialist%' OR " +
           "LOWER(m.designationFr) LIKE '%technicien%' OR LOWER(m.designationFr) LIKE '%technician%'")
    Page<MilitaryCategory> findSpecialistCategories(Pageable pageable);

    /**
     * Find medical categories (based on French designation patterns)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%médical%' OR LOWER(m.designationFr) LIKE '%medical%' OR " +
           "LOWER(m.designationFr) LIKE '%santé%' OR LOWER(m.designationFr) LIKE '%infirmier%'")
    Page<MilitaryCategory> findMedicalCategories(Pageable pageable);

    /**
     * Find administrative categories (based on French designation patterns)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%administratif%' OR LOWER(m.designationFr) LIKE '%administrative%' OR " +
           "LOWER(m.designationFr) LIKE '%civil%' OR LOWER(m.designationFr) LIKE '%clerical%'")
    Page<MilitaryCategory> findAdministrativeCategories(Pageable pageable);

    /**
     * Find reserve categories (based on French designation patterns)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%réserve%' OR LOWER(m.designationFr) LIKE '%reserve%' OR " +
           "LOWER(m.designationFr) LIKE '%auxiliaire%' OR LOWER(m.designationFr) LIKE '%auxiliary%'")
    Page<MilitaryCategory> findReserveCategories(Pageable pageable);

    /**
     * Find cadet categories (based on French designation patterns)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%élève%' OR LOWER(m.designationFr) LIKE '%étudiant%' OR " +
           "LOWER(m.designationFr) LIKE '%cadet%' OR LOWER(m.designationFr) LIKE '%student%'")
    Page<MilitaryCategory> findCadetCategories(Pageable pageable);

    /**
     * Find retired categories (based on French designation patterns)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%retraité%' OR LOWER(m.designationFr) LIKE '%retired%' OR " +
           "LOWER(m.designationFr) LIKE '%honoraire%' OR LOWER(m.designationFr) LIKE '%emeritus%'")
    Page<MilitaryCategory> findRetiredCategories(Pageable pageable);

    /**
     * Find active duty categories (officers, NCOs, enlisted, specialists, medical)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%officier%' OR LOWER(m.designationFr) LIKE '%sous-officier%' OR " +
           "LOWER(m.designationFr) LIKE '%soldat%' OR LOWER(m.designationFr) LIKE '%spécialisé%' OR " +
           "LOWER(m.designationFr) LIKE '%médical%'")
    Page<MilitaryCategory> findActiveDutyCategories(Pageable pageable);

    /**
     * Find command categories (officers and senior NCOs)
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%officier%' OR LOWER(m.designationFr) LIKE '%commandant%' OR " +
           "LOWER(m.designationFr) LIKE '%adjudant%'")
    Page<MilitaryCategory> findCommandCategories(Pageable pageable);

    /**
     * Find categories requiring security clearance
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%officier%' OR LOWER(m.designationFr) LIKE '%sous-officier%' OR " +
           "LOWER(m.designationFr) LIKE '%spécialisé%'")
    Page<MilitaryCategory> findSecurityClearanceCategories(Pageable pageable);

    /**
     * Find operational categories
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%officier%' OR LOWER(m.designationFr) LIKE '%sous-officier%' OR " +
           "LOWER(m.designationFr) LIKE '%soldat%' OR LOWER(m.designationFr) LIKE '%spécialisé%'")
    Page<MilitaryCategory> findOperationalCategories(Pageable pageable);

    /**
     * Find military categories ordered by designation in specific language
     */
    @Query("SELECT m FROM MilitaryCategory m ORDER BY m.designationAr ASC")
    Page<MilitaryCategory> findAllOrderByDesignationAr(Pageable pageable);

    @Query("SELECT m FROM MilitaryCategory m ORDER BY m.designationEn ASC")
    Page<MilitaryCategory> findAllOrderByDesignationEn(Pageable pageable);

    /**
     * Count categories by type
     */
    @Query("SELECT COUNT(m) FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%officier%' OR LOWER(m.designationFr) LIKE '%officer%'")
    Long countOfficerCategories();

    @Query("SELECT COUNT(m) FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%sous-officier%' OR LOWER(m.designationFr) LIKE '%sergent%'")
    Long countNCOCategories();

    @Query("SELECT COUNT(m) FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%soldat%' OR LOWER(m.designationFr) LIKE '%caporal%'")
    Long countEnlistedCategories();

    @Query("SELECT COUNT(m) FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%spécialisé%' OR LOWER(m.designationFr) LIKE '%technicien%'")
    Long countSpecialistCategories();

    @Query("SELECT COUNT(m) FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%médical%' OR LOWER(m.designationFr) LIKE '%santé%'")
    Long countMedicalCategories();

    /**
     * Find categories by hierarchy level
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%officier%'")
    Page<MilitaryCategory> findCommissionedPersonnel(Pageable pageable);

    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%sous-officier%' OR LOWER(m.designationFr) LIKE '%sergent%'")
    Page<MilitaryCategory> findNonCommissionedPersonnel(Pageable pageable);

    @Query("SELECT m FROM MilitaryCategory m WHERE " +
           "LOWER(m.designationFr) LIKE '%soldat%' OR LOWER(m.designationFr) LIKE '%caporal%'")
    Page<MilitaryCategory> findEnlistedPersonnel(Pageable pageable);

    /**
     * Search categories by category type pattern
     */
    @Query("SELECT m FROM MilitaryCategory m WHERE LOWER(m.designationFr) LIKE %:typePattern%")
    Page<MilitaryCategory> findByCategoryType(@Param("typePattern") String typePattern, Pageable pageable);
}
