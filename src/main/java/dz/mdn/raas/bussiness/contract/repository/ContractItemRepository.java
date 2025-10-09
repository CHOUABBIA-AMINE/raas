package dz.mdn.raas.bussiness.contract.repository;

import dz.mdn.raas.bussiness.contract.model.Contract;
import dz.mdn.raas.bussiness.contract.model.ContractItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for ContractItem entity operations
 * Manages contract item data access and queries
 */
@Repository
public interface ContractItemRepository extends JpaRepository<ContractItem, Long> {

    /**
     * Find contract items by contract
     * @param contract the contract to filter by
     * @return list of contract items for the contract
     */
    List<ContractItem> findByContract(Contract contract);

    /**
     * Find contract items by description containing (case insensitive)
     * @param description the partial description to search for
     * @return list of contract items containing the description
     */
    @Query("SELECT ci FROM ContractItem ci WHERE LOWER(ci.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<ContractItem> findByDescriptionContainingIgnoreCase(@Param("description") String description);

    /**
     * Find contract items by quantity range
     * @param minQuantity minimum quantity
     * @param maxQuantity maximum quantity
     * @return list of contract items within the quantity range
     */
    @Query("SELECT ci FROM ContractItem ci WHERE ci.quantity BETWEEN :minQuantity AND :maxQuantity")
    List<ContractItem> findByQuantityBetween(@Param("minQuantity") BigDecimal minQuantity, 
                                           @Param("maxQuantity") BigDecimal maxQuantity);

    /**
     * Find contract items by unit price range
     * @param minPrice minimum unit price
     * @param maxPrice maximum unit price
     * @return list of contract items within the price range
     */
    @Query("SELECT ci FROM ContractItem ci WHERE ci.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<ContractItem> findByUnitPriceBetween(@Param("minPrice") BigDecimal minPrice, 
                                             @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Find contract items by contract ordered by description
     * @param contract the contract to filter by
     * @return list of contract items ordered by description
     */
    List<ContractItem> findByContractOrderByDescriptionAsc(Contract contract);

    /**
     * Calculate total amount for contract
     * @param contract the contract to calculate total for
     * @return total amount of all items in the contract
     */
    @Query("SELECT SUM(ci.quantity * ci.unitPrice) FROM ContractItem ci WHERE ci.contract = :contract")
    BigDecimal calculateTotalAmountByContract(@Param("contract") Contract contract);

    /**
     * Count items by contract
     * @param contract the contract to count items for
     * @return number of items in the contract
     */
    long countByContract(Contract contract);

    /**
     * Find contract items with quantity greater than
     * @param quantity the minimum quantity threshold
     * @return list of contract items with quantity greater than threshold
     */
    List<ContractItem> findByQuantityGreaterThan(BigDecimal quantity);

    /**
     * Find contract items with unit price greater than
     * @param unitPrice the minimum unit price threshold
     * @return list of contract items with unit price greater than threshold
     */
    List<ContractItem> findByUnitPriceGreaterThan(BigDecimal unitPrice);

    /**
     * Delete contract items by contract
     * @param contract the contract to delete items for
     */
    void deleteByContract(Contract contract);
}