package dz.mdn.raas.bussiness.provider.repository;

import dz.mdn.raas.bussiness.provider.model.ExclusionType;
import dz.mdn.raas.bussiness.provider.model.Provider;
import dz.mdn.raas.bussiness.provider.model.ProviderExclusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for ProviderExclusion entity operations
 * Manages provider exclusion data access and queries
 */
@Repository
public interface ProviderExclusionRepository extends JpaRepository<ProviderExclusion, Long> {

    /**
     * Find provider exclusions by provider
     * @param provider the provider to filter by
     * @return list of exclusions for the provider
     */
    List<ProviderExclusion> findByProvider(Provider provider);

    /**
     * Find provider exclusions by exclusion type
     * @param exclusionType the exclusion type to filter by
     * @return list of provider exclusions with matching type
     */
    List<ProviderExclusion> findByExclusionType(ExclusionType exclusionType);

    /**
     * Find provider exclusions by start date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of provider exclusions within the start date range
     */
    @Query("SELECT pe FROM ProviderExclusion pe WHERE pe.startDate BETWEEN :startDate AND :endDate")
    List<ProviderExclusion> findByStartDateBetween(@Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);

    /**
     * Find provider exclusions by end date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of provider exclusions within the end date range
     */
    @Query("SELECT pe FROM ProviderExclusion pe WHERE pe.endDate BETWEEN :startDate AND :endDate")
    List<ProviderExclusion> findByEndDateBetween(@Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate);

    /**
     * Find active provider exclusions (current date within exclusion period)
     * @return list of active provider exclusions
     */
    @Query("SELECT pe FROM ProviderExclusion pe WHERE pe.startDate <= CURRENT_DATE AND (pe.endDate IS NULL OR pe.endDate >= CURRENT_DATE)")
    List<ProviderExclusion> findActiveExclusions();

    /**
     * Find active provider exclusions for specific provider
     * @param provider the provider to filter by
     * @return list of active exclusions for the provider
     */
    @Query("SELECT pe FROM ProviderExclusion pe WHERE pe.provider = :provider AND pe.startDate <= CURRENT_DATE AND (pe.endDate IS NULL OR pe.endDate >= CURRENT_DATE)")
    List<ProviderExclusion> findActiveExclusionsByProvider(@Param("provider") Provider provider);

    /**
     * Find expired provider exclusions
     * @return list of expired provider exclusions
     */
    @Query("SELECT pe FROM ProviderExclusion pe WHERE pe.endDate < CURRENT_DATE")
    List<ProviderExclusion> findExpiredExclusions();

    /**
     * Find provider exclusions by provider and exclusion type
     * @param provider the provider to filter by
     * @param exclusionType the exclusion type to filter by
     * @return list of provider exclusions matching both criteria
     */
    List<ProviderExclusion> findByProviderAndExclusionType(Provider provider, ExclusionType exclusionType);

    /**
     * Count exclusions by provider
     * @param provider the provider to count exclusions for
     * @return count of exclusions for the provider
     */
    long countByProvider(Provider provider);

    /**
     * Count active exclusions by provider
     * @param provider the provider to count active exclusions for
     * @return count of active exclusions for the provider
     */
    @Query("SELECT COUNT(pe) FROM ProviderExclusion pe WHERE pe.provider = :provider AND pe.startDate <= CURRENT_DATE AND (pe.endDate IS NULL OR pe.endDate >= CURRENT_DATE)")
    long countActiveExclusionsByProvider(@Param("provider") Provider provider);

    /**
     * Check if provider is currently excluded
     * @param provider the provider to check
     * @return true if provider has active exclusions, false otherwise
     */
    @Query("SELECT COUNT(pe) > 0 FROM ProviderExclusion pe WHERE pe.provider = :provider AND pe.startDate <= CURRENT_DATE AND (pe.endDate IS NULL OR pe.endDate >= CURRENT_DATE)")
    boolean isProviderExcluded(@Param("provider") Provider provider);

    /**
     * Find provider exclusions by provider ordered by start date desc
     * @param provider the provider to filter by
     * @return list of provider exclusions ordered by start date descending
     */
    List<ProviderExclusion> findByProviderOrderByStartDateDesc(Provider provider);
}