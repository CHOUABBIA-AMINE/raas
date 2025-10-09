package dz.mdn.raas.bussiness.contract.repository;

import dz.mdn.raas.bussiness.contract.model.Contract;
import dz.mdn.raas.bussiness.contract.model.ContractType;
import dz.mdn.raas.bussiness.core.model.ApprovalStatus;
import dz.mdn.raas.bussiness.core.model.RealizationStatus;
import dz.mdn.raas.bussiness.provider.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Contract entity operations
 * Manages contract data access and complex queries
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    /**
     * Find contract by reference number
     * @param reference the contract reference to search for
     * @return optional contract with matching reference
     */
    Optional<Contract> findByReference(String reference);

    /**
     * Find contracts by title containing (case insensitive)
     * @param title the partial title to search for
     * @return list of contracts containing the title
     */
    @Query("SELECT c FROM Contract c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Contract> findByTitleContainingIgnoreCase(@Param("title") String title);

    /**
     * Find contracts by provider
     * @param provider the provider to filter by
     * @return list of contracts with matching provider
     */
    List<Contract> findByProvider(Provider provider);

    /**
     * Find contracts by contract type
     * @param contractType the contract type to filter by
     * @return list of contracts with matching type
     */
    List<Contract> findByContractType(ContractType contractType);

    /**
     * Find contracts by approval status
     * @param approvalStatus the approval status to filter by
     * @return list of contracts with matching approval status
     */
    List<Contract> findByApprovalStatus(ApprovalStatus approvalStatus);

    /**
     * Find contracts by realization status
     * @param realizationStatus the realization status to filter by
     * @return list of contracts with matching realization status
     */
    List<Contract> findByRealizationStatus(RealizationStatus realizationStatus);

    /**
     * Find contracts by amount range
     * @param minAmount minimum contract amount
     * @param maxAmount maximum contract amount
     * @return list of contracts within the amount range
     */
    @Query("SELECT c FROM Contract c WHERE c.amount BETWEEN :minAmount AND :maxAmount")
    List<Contract> findByAmountBetween(@Param("minAmount") BigDecimal minAmount, 
                                      @Param("maxAmount") BigDecimal maxAmount);

    /**
     * Find contracts by signing date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of contracts within the date range
     */
    @Query("SELECT c FROM Contract c WHERE c.signingDate BETWEEN :startDate AND :endDate")
    List<Contract> findBySigningDateBetween(@Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);

    /**
     * Find contracts with execution deadline after specified date
     * @param date the date to compare against
     * @return list of contracts with deadline after the date
     */
    @Query("SELECT c FROM Contract c WHERE c.executionDeadline > :date")
    List<Contract> findByExecutionDeadlineAfter(@Param("date") LocalDate date);

    /**
     * Find active contracts (execution deadline not passed)
     * @return list of active contracts
     */
    @Query("SELECT c FROM Contract c WHERE c.executionDeadline >= CURRENT_DATE")
    List<Contract> findActiveContracts();

    /**
     * Find expired contracts (execution deadline passed)
     * @return list of expired contracts
     */
    @Query("SELECT c FROM Contract c WHERE c.executionDeadline < CURRENT_DATE")
    List<Contract> findExpiredContracts();

    /**
     * Find contracts ordered by signing date desc
     * @param pageable pagination information
     * @return paginated list of contracts ordered by signing date
     */
    Page<Contract> findAllByOrderBySigningDateDesc(Pageable pageable);

    /**
     * Find contracts by amount greater than
     * @param amount the minimum amount threshold
     * @return list of contracts with amount greater than threshold
     */
    List<Contract> findByAmountGreaterThan(BigDecimal amount);

    /**
     * Count contracts by approval status
     * @param approvalStatus the approval status to count
     * @return count of contracts with the status
     */
    long countByApprovalStatus(ApprovalStatus approvalStatus);

    /**
     * Count contracts by provider
     * @param provider the provider to count contracts for
     * @return count of contracts with the provider
     */
    long countByProvider(Provider provider);

    /**
     * Check if contract exists by reference
     * @param reference the contract reference to check
     * @return true if exists, false otherwise
     */
    boolean existsByReference(String reference);

    /**
     * Find contracts signed in current year
     * @return list of contracts signed this year
     */
    @Query("SELECT c FROM Contract c WHERE YEAR(c.signingDate) = YEAR(CURRENT_DATE)")
    List<Contract> findSignedThisYear();

    /**
     * Find contracts by provider ordered by signing date desc
     * @param provider the provider to filter by
     * @return list of contracts ordered by signing date descending
     */
    List<Contract> findByProviderOrderBySigningDateDesc(Provider provider);
}