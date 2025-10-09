package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.Structure;
import dz.mdn.raas.common.administration.model.StructureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Structure entity operations
 * Manages organizational structure data access and queries
 */
@Repository
public interface StructureRepository extends JpaRepository<Structure, Long> {

    /**
     * Find structure by name
     * @param name the structure name to search for
     * @return optional structure with matching name
     */
    Optional<Structure> findByName(String name);

    /**
     * Find structure by code
     * @param code the structure code to search for
     * @return optional structure with matching code
     */
    Optional<Structure> findByCode(String code);

    /**
     * Find structures by structure type
     * @param structureType the structure type to filter by
     * @return list of structures of the specified type
     */
    List<Structure> findByStructureType(StructureType structureType);

    /**
     * Find structures by parent structure
     * @param parent the parent structure to filter by
     * @return list of child structures
     */
    List<Structure> findByParent(Structure parent);

    /**
     * Find root structures (no parent)
     * @return list of root structures
     */
    @Query("SELECT s FROM Structure s WHERE s.parent IS NULL")
    List<Structure> findRootStructures();

    /**
     * Find structures by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of structures containing the name
     */
    @Query("SELECT s FROM Structure s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Structure> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find structures by type ordered by name
     * @param structureType the structure type to filter by
     * @return list of structures ordered by name ascending
     */
    List<Structure> findByStructureTypeOrderByNameAsc(StructureType structureType);

    /**
     * Find structures by parent ordered by name
     * @param parent the parent structure to filter by
     * @return list of child structures ordered by name ascending
     */
    List<Structure> findByParentOrderByNameAsc(Structure parent);

    /**
     * Find all structures ordered by name
     * @return list of structures ordered by name ascending
     */
    List<Structure> findAllByOrderByNameAsc();

    /**
     * Check if structure exists by name
     * @param name the structure name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Check if structure exists by code
     * @param code the structure code to check
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * Find structure by name (case insensitive)
     * @param name the structure name to search for
     * @return optional structure with matching name
     */
    @Query("SELECT s FROM Structure s WHERE LOWER(s.name) = LOWER(:name)")
    Optional<Structure> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find structure by code (case insensitive)
     * @param code the structure code to search for
     * @return optional structure with matching code
     */
    @Query("SELECT s FROM Structure s WHERE LOWER(s.code) = LOWER(:code)")
    Optional<Structure> findByCodeIgnoreCase(@Param("code") String code);

    /**
     * Find all active structures
     * @return list of active structures
     */
    @Query("SELECT s FROM Structure s WHERE s.active = true")
    List<Structure> findAllActive();

    /**
     * Find active structures by type
     * @param structureType the structure type to filter by
     * @return list of active structures of the specified type
     */
    @Query("SELECT s FROM Structure s WHERE s.structureType = :structureType AND s.active = true")
    List<Structure> findActiveByStructureType(@Param("structureType") StructureType structureType);

    /**
     * Count structures by type
     * @param structureType the structure type to count for
     * @return count of structures of the specified type
     */
    long countByStructureType(StructureType structureType);

    /**
     * Count child structures
     * @param parent the parent structure to count children for
     * @return count of child structures
     */
    long countByParent(Structure parent);

    /**
     * Count active structures
     * @return number of active structures
     */
    @Query("SELECT COUNT(s) FROM Structure s WHERE s.active = true")
    long countActive();
}