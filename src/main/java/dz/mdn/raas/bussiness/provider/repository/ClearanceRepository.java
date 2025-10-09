package dz.mdn.raas.bussiness.provider.repository;

import dz.mdn.raas.bussiness.provider.model.Clearance;
import dz.mdn.raas.bussiness.provider.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Clearance entity operations
 * Manages clearance data access and queries
 */
@Repository
public interface ClearanceRepository extends JpaRepository<Clearance, Long> {

    /**
     * Find clearances by provider
     * @param provider the provider to filter by
     * @return list of clearances for the provider
     */
    List<Clearance> findByProvider(Provider provider);

    /**
     * Find clearance by reference number
     * @param reference the clearance reference to search for
     * @return optional clearance with matching reference
     */
    Optional<Clearance> findByReference(String reference);

    /**
     * Find clearances by issue date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of clearances within the date range
     */
    @Query("SELECT c FROM Clearance c WHERE c.issueDate BETWEEN :startDate AND :endDate")
    List<Clearance> findByIssueDateBetween(@Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);

    /**
     * Find clearances by expiry date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of clearances within the expiry date range
     */
    @Query("SELECT c FROM Clearance c WHERE c.expiryDate BETWEEN :startDate AND :endDate")
    List<Clearance> findByExpiryDateBetween(@Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);

    /**
     * Find active clearances (not expired)
     * @return list of active clearances
     */
    @Query("SELECT c FROM Clearance c WHERE c.expiryDate >= CURRENT_DATE")
    List<Clearance> findActiveClearances();

    /**
     * Find expired clearances
     * @return list of expired clearances
     */
    @Query("SELECT c FROM Clearance c WHERE c.expiryDate < CURRENT_DATE")
    List<Clearance> findExpiredClearances();

    /**
     * Find clearances expiring soon (within specified days)
     * @param days number of days to check ahead
     * @return list of clearances expiring within the specified days
     */
    @Query("SELECT c FROM Clearance c WHERE c.expiryDate BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, :days, DAY)")
    List<Clearance> findClearancesExpiringSoon(@Param("days") int days);

    /**
     * Find clearances by provider ordered by expiry date
     * @param provider the provider to filter by
     * @return list of clearances ordered by expiry date ascending
     */
    List<Clearance> findByProviderOrderByExpiryDateAsc(Provider provider);

    /**
     * Count clearances by provider
     * @param provider the provider to count clearances for
     * @return count of clearances for the provider
     */
    long countByProvider(Provider provider);

    /**
     * Check if clearance exists by reference
     * @param reference the clearance reference to check
     * @return true if exists, false otherwise
     */
    boolean existsByReference(String reference);

    /**
     * Find latest clearance for provider
     * @param provider the provider to find latest clearance for
     * @return optional latest clearance for the provider
     */
    @Query("SELECT c FROM Clearance c WHERE c.provider = :provider ORDER BY c.issueDate DESC")
    Optional<Clearance> findLatestClearanceByProvider(@Param("provider") Provider provider);

    /**
     * Count active clearances by provider
     * @param provider the provider to count active clearances for
     * @return count of active clearances for the provider
     */
    @Query("SELECT COUNT(c) FROM Clearance c WHERE c.provider = :provider AND c.expiryDate >= CURRENT_DATE")
    long countActiveClearancesByProvider(@Param("provider") Provider provider);
}