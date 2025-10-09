package dz.mdn.raas.bussiness.contract.repository;

import dz.mdn.raas.bussiness.contract.model.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ContractType entity operations
 * Manages contract type data access and queries
 */
@Repository
public interface ContractTypeRepository extends JpaRepository<ContractType, Long> {

    /**
     * Find contract type by name
     * @param name the type name to search for
     * @return optional contract type with matching name
     */
    Optional<ContractType> findByName(String name);

    /**
     * Find contract types by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of contract types containing the name
     */
    @Query("SELECT ct FROM ContractType ct WHERE LOWER(ct.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ContractType> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find all contract types ordered by name
     * @return list of contract types ordered by name ascending
     */
    List<ContractType> findAllByOrderByNameAsc();

    /**
     * Check if contract type exists by name
     * @param name the type name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find contract type by name (case insensitive)
     * @param name the type name to search for
     * @return optional contract type with matching name
     */
    @Query("SELECT ct FROM ContractType ct WHERE LOWER(ct.name) = LOWER(:name)")
    Optional<ContractType> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active contract types
     * @return list of active contract types
     */
    @Query("SELECT ct FROM ContractType ct WHERE ct.active = true")
    List<ContractType> findAllActive();

    /**
     * Count active contract types
     * @return number of active contract types
     */
    @Query("SELECT COUNT(ct) FROM ContractType ct WHERE ct.active = true")
    long countActive();
}