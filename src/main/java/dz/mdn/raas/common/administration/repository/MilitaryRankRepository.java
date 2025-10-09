package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.MilitaryCategory;
import dz.mdn.raas.common.administration.model.MilitaryRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MilitaryRank entity operations
 * Manages military rank data access and queries
 */
@Repository
public interface MilitaryRankRepository extends JpaRepository<MilitaryRank, Long> {

    /**
     * Find military rank by name
     * @param name the rank name to search for
     * @return optional military rank with matching name
     */
    Optional<MilitaryRank> findByName(String name);

    /**
     * Find military ranks by military category
     * @param militaryCategory the military category to filter by
     * @return list of military ranks in the category
     */
    List<MilitaryRank> findByMilitaryCategory(MilitaryCategory militaryCategory);

    /**
     * Find military ranks by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of military ranks containing the name
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE LOWER(mr.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MilitaryRank> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find military ranks by category ordered by name
     * @param militaryCategory the military category to filter by
     * @return list of military ranks ordered by name ascending
     */
    List<MilitaryRank> findByMilitaryCategoryOrderByNameAsc(MilitaryCategory militaryCategory);

    /**
     * Find all military ranks ordered by name
     * @return list of military ranks ordered by name ascending
     */
    List<MilitaryRank> findAllByOrderByNameAsc();

    /**
     * Check if military rank exists by name
     * @param name the rank name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find military rank by name (case insensitive)
     * @param name the rank name to search for
     * @return optional military rank with matching name
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE LOWER(mr.name) = LOWER(:name)")
    Optional<MilitaryRank> findByNameIgnoreCase(@Param("name") String name);

    /**
     * Find all active military ranks
     * @return list of active military ranks
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.active = true")
    List<MilitaryRank> findAllActive();

    /**
     * Find active military ranks by category
     * @param militaryCategory the military category to filter by
     * @return list of active military ranks in the category
     */
    @Query("SELECT mr FROM MilitaryRank mr WHERE mr.militaryCategory = :militaryCategory AND mr.active = true")
    List<MilitaryRank> findActiveByMilitaryCategory(@Param("militaryCategory") MilitaryCategory militaryCategory);

    /**
     * Count military ranks by category
     * @param militaryCategory the military category to count ranks for
     * @return count of military ranks in the category
     */
    long countByMilitaryCategory(MilitaryCategory militaryCategory);

    /**
     * Count active military ranks
     * @return number of active military ranks
     */
    @Query("SELECT COUNT(mr) FROM MilitaryRank mr WHERE mr.active = true")
    long countActive();
}