/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StructureTypeRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.StructureType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * StructureType Repository with essential CRUD operations
 * Based on exact field names: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=acronymAr, F_05=acronymEn, F_06=acronymFr
 * F_03 (designationFr) has unique constraint and is required
 * F_06 (acronymFr) has unique constraint and is required
 * F_01 (designationAr), F_02 (designationEn), F_04 (acronymAr), F_05 (acronymEn) are optional
 */
@Repository
public interface StructureTypeRepository extends JpaRepository<StructureType, Long> {

    /**
     * Find structure type by French designation (F_03) - unique field
     */
    @Query("SELECT s FROM StructureType s WHERE s.designationFr = :designationFr")
    Optional<StructureType> findByDesignationFr(@Param("designationFr") String designationFr);

    /**
     * Find structure type by French acronym (F_06) - unique field
     */
    @Query("SELECT s FROM StructureType s WHERE s.acronymFr = :acronymFr")
    Optional<StructureType> findByAcronymFr(@Param("acronymFr") String acronymFr);

    /**
     * Find structure type by Arabic designation (F_01)
     */
    @Query("SELECT s FROM StructureType s WHERE s.designationAr = :designationAr")
    Optional<StructureType> findByDesignationAr(@Param("designationAr") String designationAr);

    /**
     * Find structure type by English designation (F_02)
     */
    @Query("SELECT s FROM StructureType s WHERE s.designationEn = :designationEn")
    Optional<StructureType> findByDesignationEn(@Param("designationEn") String designationEn);

    /**
     * Find structure type by Arabic acronym (F_04)
     */
    @Query("SELECT s FROM StructureType s WHERE s.acronymAr = :acronymAr")
    Optional<StructureType> findByAcronymAr(@Param("acronymAr") String acronymAr);

    /**
     * Find structure type by English acronym (F_05)
     */
    @Query("SELECT s FROM StructureType s WHERE s.acronymEn = :acronymEn")
    Optional<StructureType> findByAcronymEn(@Param("acronymEn") String acronymEn);

    /**
     * Check if structure type exists by French designation
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM StructureType s WHERE s.designationFr = :designationFr")
    boolean existsByDesignationFr(@Param("designationFr") String designationFr);

    /**
     * Check if structure type exists by French acronym
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM StructureType s WHERE s.acronymFr = :acronymFr")
    boolean existsByAcronymFr(@Param("acronymFr") String acronymFr);

    /**
     * Check unique constraint for updates (excluding current ID) - designation
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM StructureType s WHERE s.designationFr = :designationFr AND s.id != :id")
    boolean existsByDesignationFrAndIdNot(@Param("designationFr") String designationFr, @Param("id") Long id);

    /**
     * Check unique constraint for updates (excluding current ID) - acronym
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM StructureType s WHERE s.acronymFr = :acronymFr AND s.id != :id")
    boolean existsByAcronymFrAndIdNot(@Param("acronymFr") String acronymFr, @Param("id") Long id);

    /**
     * Find all structure types with pagination ordered by French designation
     */
    @Query("SELECT s FROM StructureType s ORDER BY s.designationFr ASC")
    Page<StructureType> findAllOrderByDesignationFr(Pageable pageable);

    /**
     * Find all structure types ordered by French acronym
     */
    @Query("SELECT s FROM StructureType s ORDER BY s.acronymFr ASC")
    Page<StructureType> findAllOrderByAcronymFr(Pageable pageable);

    /**
     * Search structure types by any designation field
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "s.designationAr LIKE %:search% OR " +
           "s.designationEn LIKE %:search% OR " +
           "s.designationFr LIKE %:search%")
    Page<StructureType> searchByDesignation(@Param("search") String search, Pageable pageable);

    /**
     * Search structure types by any acronym field
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "s.acronymAr LIKE %:search% OR " +
           "s.acronymEn LIKE %:search% OR " +
           "s.acronymFr LIKE %:search%")
    Page<StructureType> searchByAcronym(@Param("search") String search, Pageable pageable);

    /**
     * Search structure types by designation or acronym
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "s.designationAr LIKE %:search% OR s.designationEn LIKE %:search% OR s.designationFr LIKE %:search% OR " +
           "s.acronymAr LIKE %:search% OR s.acronymEn LIKE %:search% OR s.acronymFr LIKE %:search%")
    Page<StructureType> searchByDesignationOrAcronym(@Param("search") String search, Pageable pageable);

    /**
     * Find structure types by French designation pattern (F_03)
     */
    @Query("SELECT s FROM StructureType s WHERE s.designationFr LIKE %:pattern%")
    Page<StructureType> findByDesignationFrContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find structure types by French acronym pattern (F_06)
     */
    @Query("SELECT s FROM StructureType s WHERE s.acronymFr LIKE %:pattern%")
    Page<StructureType> findByAcronymFrContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Count total structure types
     */
    @Query("SELECT COUNT(s) FROM StructureType s")
    Long countAllStructureTypes();

    /**
     * Find structure types that have Arabic designation
     */
    @Query("SELECT s FROM StructureType s WHERE s.designationAr IS NOT NULL AND s.designationAr != ''")
    Page<StructureType> findWithArabicDesignation(Pageable pageable);

    /**
     * Find structure types that have English designation
     */
    @Query("SELECT s FROM StructureType s WHERE s.designationEn IS NOT NULL AND s.designationEn != ''")
    Page<StructureType> findWithEnglishDesignation(Pageable pageable);

    /**
     * Find structure types that have Arabic acronym
     */
    @Query("SELECT s FROM StructureType s WHERE s.acronymAr IS NOT NULL AND s.acronymAr != ''")
    Page<StructureType> findWithArabicAcronym(Pageable pageable);

    /**
     * Find structure types that have English acronym
     */
    @Query("SELECT s FROM StructureType s WHERE s.acronymEn IS NOT NULL AND s.acronymEn != ''")
    Page<StructureType> findWithEnglishAcronym(Pageable pageable);

    /**
     * Find multilingual structure types (have at least 2 designations)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "(s.designationAr IS NOT NULL AND s.designationAr != '' AND s.designationEn IS NOT NULL AND s.designationEn != '') OR " +
           "(s.designationAr IS NOT NULL AND s.designationAr != '' AND s.designationFr IS NOT NULL AND s.designationFr != '') OR " +
           "(s.designationEn IS NOT NULL AND s.designationEn != '' AND s.designationFr IS NOT NULL AND s.designationFr != '')")
    Page<StructureType> findMultilingualStructureTypes(Pageable pageable);

    /**
     * Find command structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%commandement%' OR LOWER(s.designationFr) LIKE '%état-major%' OR " +
           "LOWER(s.designationFr) LIKE '%quartier général%' OR LOWER(s.designationFr) LIKE '%hq%'")
    Page<StructureType> findCommandStructures(Pageable pageable);

    /**
     * Find administrative structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%direction%' OR LOWER(s.designationFr) LIKE '%bureau%' OR " +
           "LOWER(s.designationFr) LIKE '%service%' OR LOWER(s.designationFr) LIKE '%département%'")
    Page<StructureType> findAdministrativeStructures(Pageable pageable);

    /**
     * Find operational structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%brigade%' OR LOWER(s.designationFr) LIKE '%régiment%' OR " +
           "LOWER(s.designationFr) LIKE '%bataillon%' OR LOWER(s.designationFr) LIKE '%escadron%'")
    Page<StructureType> findOperationalStructures(Pageable pageable);

    /**
     * Find support structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%soutien%' OR LOWER(s.designationFr) LIKE '%appui%' OR " +
           "LOWER(s.designationFr) LIKE '%logistique%' OR LOWER(s.designationFr) LIKE '%maintenance%'")
    Page<StructureType> findSupportStructures(Pageable pageable);

    /**
     * Find training structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%école%' OR LOWER(s.designationFr) LIKE '%centre de formation%' OR " +
           "LOWER(s.designationFr) LIKE '%académie%' OR LOWER(s.designationFr) LIKE '%institut%'")
    Page<StructureType> findTrainingStructures(Pageable pageable);

    /**
     * Find medical structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%hôpital%' OR LOWER(s.designationFr) LIKE '%infirmerie%' OR " +
           "LOWER(s.designationFr) LIKE '%santé%' OR LOWER(s.designationFr) LIKE '%médical%'")
    Page<StructureType> findMedicalStructures(Pageable pageable);

    /**
     * Find technical structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%technique%' OR LOWER(s.designationFr) LIKE '%ingénierie%' OR " +
           "LOWER(s.designationFr) LIKE '%maintenance%' OR LOWER(s.designationFr) LIKE '%réparation%'")
    Page<StructureType> findTechnicalStructures(Pageable pageable);

    /**
     * Find intelligence structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%renseignement%' OR LOWER(s.designationFr) LIKE '%intelligence%' OR " +
           "LOWER(s.designationFr) LIKE '%sécurité%' OR LOWER(s.designationFr) LIKE '%surveillance%'")
    Page<StructureType> findIntelligenceStructures(Pageable pageable);

    /**
     * Find communications structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%communication%' OR LOWER(s.designationFr) LIKE '%transmissions%' OR " +
           "LOWER(s.designationFr) LIKE '%télécommunications%' OR LOWER(s.designationFr) LIKE '%signal%'")
    Page<StructureType> findCommunicationsStructures(Pageable pageable);

    /**
     * Find logistics structures (based on French designation patterns)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%logistique%' OR LOWER(s.designationFr) LIKE '%approvisionnement%' OR " +
           "LOWER(s.designationFr) LIKE '%transport%' OR LOWER(s.designationFr) LIKE '%distribution%'")
    Page<StructureType> findLogisticsStructures(Pageable pageable);

    /**
     * Find strategic level structures
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%état-major%' OR LOWER(s.designationFr) LIKE '%commandement supérieur%' OR " +
           "LOWER(s.designationFr) LIKE '%direction générale%' OR LOWER(s.designationFr) LIKE '%hq%'")
    Page<StructureType> findStrategicLevelStructures(Pageable pageable);

    /**
     * Find operational level structures
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%brigade%' OR LOWER(s.designationFr) LIKE '%division%' OR " +
           "LOWER(s.designationFr) LIKE '%région%' OR LOWER(s.designationFr) LIKE '%zone%'")
    Page<StructureType> findOperationalLevelStructures(Pageable pageable);

    /**
     * Find tactical level structures
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%régiment%' OR LOWER(s.designationFr) LIKE '%bataillon%' OR " +
           "LOWER(s.designationFr) LIKE '%escadron%' OR LOWER(s.designationFr) LIKE '%compagnie%'")
    Page<StructureType> findTacticalLevelStructures(Pageable pageable);

    /**
     * Find unit level structures
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%section%' OR LOWER(s.designationFr) LIKE '%peloton%' OR " +
           "LOWER(s.designationFr) LIKE '%équipe%' OR LOWER(s.designationFr) LIKE '%groupe%'")
    Page<StructureType> findUnitLevelStructures(Pageable pageable);

    /**
     * Find structures requiring security clearance
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%commandement%' OR LOWER(s.designationFr) LIKE '%renseignement%' OR " +
           "LOWER(s.designationFr) LIKE '%intelligence%' OR LOWER(s.designationFr) LIKE '%brigade%' OR " +
           "LOWER(s.designationFr) LIKE '%régiment%'")
    Page<StructureType> findSecurityClearanceStructures(Pageable pageable);

    /**
     * Find deployable structures (mobile and semi-mobile)
     */
    @Query("SELECT s FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%brigade%' OR LOWER(s.designationFr) LIKE '%régiment%' OR " +
           "LOWER(s.designationFr) LIKE '%bataillon%' OR LOWER(s.designationFr) LIKE '%escadron%' OR " +
           "LOWER(s.designationFr) LIKE '%soutien%' OR LOWER(s.designationFr) LIKE '%logistique%'")
    Page<StructureType> findDeployableStructures(Pageable pageable);

    /**
     * Find structure types ordered by designation in specific language
     */
    @Query("SELECT s FROM StructureType s ORDER BY s.designationAr ASC")
    Page<StructureType> findAllOrderByDesignationAr(Pageable pageable);

    @Query("SELECT s FROM StructureType s ORDER BY s.designationEn ASC")
    Page<StructureType> findAllOrderByDesignationEn(Pageable pageable);

    /**
     * Count structures by category
     */
    @Query("SELECT COUNT(s) FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%commandement%' OR LOWER(s.designationFr) LIKE '%état-major%'")
    Long countCommandStructures();

    @Query("SELECT COUNT(s) FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%direction%' OR LOWER(s.designationFr) LIKE '%bureau%'")
    Long countAdministrativeStructures();

    @Query("SELECT COUNT(s) FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%brigade%' OR LOWER(s.designationFr) LIKE '%régiment%'")
    Long countOperationalStructures();

    @Query("SELECT COUNT(s) FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%soutien%' OR LOWER(s.designationFr) LIKE '%logistique%'")
    Long countSupportStructures();

    @Query("SELECT COUNT(s) FROM StructureType s WHERE " +
           "LOWER(s.designationFr) LIKE '%école%' OR LOWER(s.designationFr) LIKE '%formation%'")
    Long countTrainingStructures();

    /**
     * Search structures by category pattern
     */
    @Query("SELECT s FROM StructureType s WHERE LOWER(s.designationFr) LIKE %:categoryPattern%")
    Page<StructureType> findByStructureCategory(@Param("categoryPattern") String categoryPattern, Pageable pageable);

    /**
     * Find structures by organizational level pattern
     */
    @Query("SELECT s FROM StructureType s WHERE LOWER(s.designationFr) LIKE %:levelPattern%")
    Page<StructureType> findByOrganizationalLevel(@Param("levelPattern") String levelPattern, Pageable pageable);
}
