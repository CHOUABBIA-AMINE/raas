package dz.mdn.raas.bussiness.contract.repository;

import dz.mdn.raas.bussiness.contract.model.ContractPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ContractPhase entity operations
 * Manages contract phase data access and queries
 */
@Repository
public interface ContractPhaseRepository extends JpaRepository<ContractPhase, Long> {

    /**
     * Find contract phase by name
     * @param name the phase name to search for
     * @return optional contract phase with matching name
     */
    Optional<ContractPhase> findByName(String name);

    /**
     * Find contract phases by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of contract phases containing the name
     */
    @Query("SELECT cp FROM ContractPhase cp WHERE LOWER(cp.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ContractPhase> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all contract phases ordered by name
     * @return list of contract phases ordered by name ascending
     */
    List<ContractPhase> findAllByOrderByNameAsc();

    /**
     * Check if contract phase exists by name
     * @param name the phase name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find contract phase by name (case insensitive)
     * @param name the phase name to search for
     * @return optional contract phase with matching name
     */
    @Query("SELECT cp FROM ContractPhase cp WHERE LOWER(cp.name) = LOWER(:name)")
    Optional<ContractPhase> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active contract phases
     * @return list of active contract phases
     */
    @Query("SELECT cp FROM ContractPhase cp WHERE cp.active = true")
    List<ContractPhase> findAllActive();

    /**
     * Count active contract phases
     * @return number of active contract phases
     */
    @Query("SELECT COUNT(cp) FROM ContractPhase cp WHERE cp.active = true")
    long countActive();
}