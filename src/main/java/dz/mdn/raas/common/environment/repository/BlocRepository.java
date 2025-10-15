/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BlocRepository
 *	@CreatedOn	: 06-26-2025
 *
 *	@Type		: Class
 *	@Layaer		: Model
 *	@Package	: Common / Environment
 *
 **/

package dz.mdn.raas.common.environment.repository;

import dz.mdn.raas.common.environment.model.Bloc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlocRepository extends JpaRepository<Bloc, Long> {

    @Query("SELECT b FROM Bloc b WHERE b.codeAr = :codeAr")
    Optional<Bloc> findByCodeAr(@Param("codeAr") String codeAr);

    @Query("SELECT b FROM Bloc b WHERE b.codeLt = :codeLt")
    Optional<Bloc> findByCodeLt(@Param("codeLt") String codeLt);

    @Query("SELECT b FROM Bloc b WHERE b.designationFr = :designationFr")
    Optional<Bloc> findByDesignationFr(@Param("designationFr") String designationFr);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Bloc b WHERE b.codeAr = :codeAr")
    boolean existsByCodeAr(@Param("codeAr") String codeAr);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Bloc b WHERE b.codeLt = :codeLt")
    boolean existsByCodeLt(@Param("codeLt") String codeLt);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Bloc b WHERE b.designationFr = :designationFr")
    boolean existsByDesignationFr(@Param("designationFr") String designationFr);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Bloc b WHERE b.codeAr = :codeAr AND b.id != :id")
    boolean existsByCodeArAndIdNot(@Param("codeAr") String codeAr, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Bloc b WHERE b.codeLt = :codeLt AND b.id != :id")
    boolean existsByCodeLtAndIdNot(@Param("codeLt") String codeLt, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Bloc b WHERE b.designationFr = :designationFr AND b.id != :id")
    boolean existsByDesignationFrAndIdNot(@Param("designationFr") String designationFr, @Param("id") Long id);

    Page<Bloc> findAll(Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE b.designationAr LIKE %:designationAr%")
    Page<Bloc> findByDesignationArContaining(@Param("designationAr") String designationAr, Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE b.designationEn LIKE %:designationEn%")
    Page<Bloc> findByDesignationEnContaining(@Param("designationEn") String designationEn, Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE b.designationFr LIKE %:designationFr%")
    Page<Bloc> findByDesignationFrContaining(@Param("designationFr") String designationFr, Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE b.codeAr LIKE %:codeAr%")
    Page<Bloc> findByCodeArContaining(@Param("codeAr") String codeAr, Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE b.codeLt LIKE %:codeLt%")
    Page<Bloc> findByCodeLtContaining(@Param("codeLt") String codeLt, Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE " +
           "b.codeAr LIKE %:search% OR " +
           "b.codeLt LIKE %:search% OR " +
           "b.designationAr LIKE %:search% OR " +
           "b.designationEn LIKE %:search% OR " +
           "b.designationFr LIKE %:search%")
    Page<Bloc> searchByAnyField(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Bloc b")
    Long countAllBlocs();

    @Query("SELECT b FROM Bloc b ORDER BY b.codeLt ASC")
    Page<Bloc> findAllOrderByCodeLt(Pageable pageable);

    @Query("SELECT b FROM Bloc b ORDER BY b.designationFr ASC")
    Page<Bloc> findAllOrderByDesignationFr(Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE b.designationAr IS NOT NULL AND b.designationAr != ''")
    Page<Bloc> findWithArabicDesignation(Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE b.designationEn IS NOT NULL AND b.designationEn != ''")
    Page<Bloc> findWithEnglishDesignation(Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE " +
           "(b.designationAr IS NOT NULL AND b.designationAr != '' AND b.designationEn IS NOT NULL AND b.designationEn != '') OR " +
           "(b.designationAr IS NOT NULL AND b.designationAr != '' AND b.designationFr IS NOT NULL AND b.designationFr != '') OR " +
           "(b.designationEn IS NOT NULL AND b.designationEn != '' AND b.designationFr IS NOT NULL AND b.designationFr != '')")
    Page<Bloc> findMultilingualBlocs(Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE b.codeAr LIKE %:code% OR b.codeLt LIKE %:code%")
    Page<Bloc> findByCodePattern(@Param("code") String code, Pageable pageable);

    @Query("SELECT b FROM Bloc b WHERE " +
           "b.designationAr LIKE %:designation% OR " +
           "b.designationEn LIKE %:designation% OR " +
           "b.designationFr LIKE %:designation%")
    Page<Bloc> findByDesignationPattern(@Param("designation") String designation, Pageable pageable);
}
