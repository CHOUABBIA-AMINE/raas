package dz.mdn.raas.bussiness.contract.repository;

import dz.mdn.raas.bussiness.contract.model.ContractStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ContractStep entity operations
 * Manages contract step data access and queries
 */
@Repository
public interface ContractStepRepository extends JpaRepository<ContractStep, Long> {

    /**
     * Find contract step by name
     * @param name the step name to search for
     * @return optional contract step with matching name
     */
    Optional<ContractStep> findByName(String name);

    /**
     * Find contract steps by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of contract steps containing the name
     */
    @Query("SELECT cs FROM ContractStep cs WHERE LOWER(cs.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ContractStep> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all contract steps ordered by name
     * @return list of contract steps ordered by name ascending
     */
    List<ContractStep> findAllByOrderByNameAsc();

    /**
     * Check if contract step exists by name
     * @param name the step name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find contract step by name (case insensitive)
     * @param name the step name to search for
     * @return optional contract step with matching name
     */
    @Query("SELECT cs FROM ContractStep cs WHERE LOWER(cs.name) = LOWER(:name)")
    Optional<ContractStep> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active contract steps
     * @return list of active contract steps
     */
    @Query("SELECT cs FROM ContractStep cs WHERE cs.active = true")
    List<ContractStep> findAllActive();

    /**
     * Count active contract steps
     * @return number of active contract steps
     */
    @Query("SELECT COUNT(cs) FROM ContractStep cs WHERE cs.active = true")
    long countActive();
}