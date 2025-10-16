/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: CurrencyRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Repository
 *	@Pakage		: Business / Core
 *
 **/

package dz.mdn.raas.business.core.repository;

import dz.mdn.raas.business.core.model.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Currency Repository with essential CRUD operations
 * Based on exact field names: F_00=id, F_01=designationAr, F_02=designationEn, 
 * F_03=designationFr, F_04=codeAr, F_05=codeLt
 * All fields F_01 through F_05 have unique constraints and are required
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * Find currency by Arabic designation (F_01) - unique field
     */
    @Query("SELECT c FROM Currency c WHERE c.designationAr = :designationAr")
    Optional<Currency> findByDesignationAr(@Param("designationAr") String designationAr);

    /**
     * Find currency by English designation (F_02) - unique field
     */
    @Query("SELECT c FROM Currency c WHERE c.designationEn = :designationEn")
    Optional<Currency> findByDesignationEn(@Param("designationEn") String designationEn);

    /**
     * Find currency by French designation (F_03) - unique field
     */
    @Query("SELECT c FROM Currency c WHERE c.designationFr = :designationFr")
    Optional<Currency> findByDesignationFr(@Param("designationFr") String designationFr);

    /**
     * Find currency by Arabic code (F_04) - unique field
     */
    @Query("SELECT c FROM Currency c WHERE c.codeAr = :codeAr")
    Optional<Currency> findByCodeAr(@Param("codeAr") String codeAr);

    /**
     * Find currency by Latin code (F_05) - unique field
     */
    @Query("SELECT c FROM Currency c WHERE c.codeLt = :codeLt")
    Optional<Currency> findByCodeLt(@Param("codeLt") String codeLt);

    /**
     * Check unique constraints for creation
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.designationAr = :designationAr")
    boolean existsByDesignationAr(@Param("designationAr") String designationAr);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.designationEn = :designationEn")
    boolean existsByDesignationEn(@Param("designationEn") String designationEn);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.designationFr = :designationFr")
    boolean existsByDesignationFr(@Param("designationFr") String designationFr);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.codeAr = :codeAr")
    boolean existsByCodeAr(@Param("codeAr") String codeAr);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.codeLt = :codeLt")
    boolean existsByCodeLt(@Param("codeLt") String codeLt);

    /**
     * Check unique constraints for updates (excluding current ID)
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.designationAr = :designationAr AND c.id != :id")
    boolean existsByDesignationArAndIdNot(@Param("designationAr") String designationAr, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.designationEn = :designationEn AND c.id != :id")
    boolean existsByDesignationEnAndIdNot(@Param("designationEn") String designationEn, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.designationFr = :designationFr AND c.id != :id")
    boolean existsByDesignationFrAndIdNot(@Param("designationFr") String designationFr, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.codeAr = :codeAr AND c.id != :id")
    boolean existsByCodeArAndIdNot(@Param("codeAr") String codeAr, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Currency c WHERE c.codeLt = :codeLt AND c.id != :id")
    boolean existsByCodeLtAndIdNot(@Param("codeLt") String codeLt, @Param("id") Long id);

    /**
     * Find all currencies with pagination ordered by Latin code
     */
    @Query("SELECT c FROM Currency c ORDER BY c.codeLt ASC")
    Page<Currency> findAllOrderByCodeLt(Pageable pageable);

    /**
     * Search currencies by any designation field
     */
    @Query("SELECT c FROM Currency c WHERE " +
           "c.designationAr LIKE %:search% OR " +
           "c.designationEn LIKE %:search% OR " +
           "c.designationFr LIKE %:search%")
    Page<Currency> searchByDesignation(@Param("search") String search, Pageable pageable);

    /**
     * Search currencies by any code field
     */
    @Query("SELECT c FROM Currency c WHERE " +
           "c.codeAr LIKE %:search% OR " +
           "c.codeLt LIKE %:search%")
    Page<Currency> searchByCode(@Param("search") String search, Pageable pageable);

    /**
     * Search currencies by any field (codes and designations)
     */
    @Query("SELECT c FROM Currency c WHERE " +
           "c.designationAr LIKE %:search% OR " +
           "c.designationEn LIKE %:search% OR " +
           "c.designationFr LIKE %:search% OR " +
           "c.codeAr LIKE %:search% OR " +
           "c.codeLt LIKE %:search%")
    Page<Currency> searchByAnyField(@Param("search") String search, Pageable pageable);

    /**
     * Find currencies by Latin code pattern (for ISO currency codes)
     */
    @Query("SELECT c FROM Currency c WHERE c.codeLt LIKE %:pattern%")
    Page<Currency> findByCodeLtContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find currencies by Arabic designation pattern
     */
    @Query("SELECT c FROM Currency c WHERE c.designationAr LIKE %:pattern%")
    Page<Currency> findByDesignationArContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find currencies by English designation pattern
     */
    @Query("SELECT c FROM Currency c WHERE c.designationEn LIKE %:pattern%")
    Page<Currency> findByDesignationEnContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find currencies by French designation pattern
     */
    @Query("SELECT c FROM Currency c WHERE c.designationFr LIKE %:pattern%")
    Page<Currency> findByDesignationFrContaining(@Param("pattern") String pattern, Pageable pageable);

    /**
     * Find major international currencies (USD, EUR, GBP, etc.)
     */
    @Query("SELECT c FROM Currency c WHERE c.codeLt IN ('USD', 'EUR', 'GBP', 'JPY', 'CHF', 'CAD', 'AUD')")
    Page<Currency> findMajorCurrencies(Pageable pageable);

    /**
     * Find regional currencies (Africa, Middle East)
     */
    @Query("SELECT c FROM Currency c WHERE c.codeLt IN ('DZD', 'MAD', 'TND', 'EGP', 'SAR', 'AED', 'LBP', 'JOD', 'KWD', 'QAR', 'BHD', 'OMR')")
    Page<Currency> findRegionalCurrencies(Pageable pageable);

    /**
     * Count total currencies
     */
    @Query("SELECT COUNT(c) FROM Currency c")
    Long countAllCurrencies();

    /**
     * Find currencies ordered by specific language designation
     */
    @Query("SELECT c FROM Currency c ORDER BY c.designationAr ASC")
    Page<Currency> findAllOrderByDesignationAr(Pageable pageable);

    @Query("SELECT c FROM Currency c ORDER BY c.designationEn ASC")
    Page<Currency> findAllOrderByDesignationEn(Pageable pageable);

    @Query("SELECT c FROM Currency c ORDER BY c.designationFr ASC")
    Page<Currency> findAllOrderByDesignationFr(Pageable pageable);

    /**
     * Check if Latin code follows ISO 4217 pattern (3 uppercase letters)
     */
    @Query("SELECT c FROM Currency c WHERE c.codeLt REGEXP '^[A-Z]{3}$'")
    Page<Currency> findISOStandardCurrencies(Pageable pageable);

    /**
     * Find currencies with specific code length
     */
    @Query("SELECT c FROM Currency c WHERE LENGTH(c.codeLt) = :length")
    Page<Currency> findByCodeLength(@Param("length") int length, Pageable pageable);

    /**
     * Find currencies starting with specific letters (for grouping)
     */
    @Query("SELECT c FROM Currency c WHERE c.codeLt LIKE :prefix%")
    Page<Currency> findByCodePrefix(@Param("prefix") String prefix, Pageable pageable);

    /**
     * Check for potential duplicate entries (same Latin code but different case)
     */
    @Query("SELECT c FROM Currency c WHERE UPPER(c.codeLt) = UPPER(:codeLt)")
    Page<Currency> findPotentialDuplicatesByCodeLt(@Param("codeLt") String codeLt, Pageable pageable);
}
