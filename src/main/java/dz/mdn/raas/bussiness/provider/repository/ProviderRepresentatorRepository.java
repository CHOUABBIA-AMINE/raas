package dz.mdn.raas.bussiness.provider.repository;

import dz.mdn.raas.bussiness.provider.model.Provider;
import dz.mdn.raas.bussiness.provider.model.ProviderRepresentator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ProviderRepresentator entity operations
 * Manages provider representator data access and queries
 */
@Repository
public interface ProviderRepresentatorRepository extends JpaRepository<ProviderRepresentator, Long> {

    /**
     * Find provider representators by provider
     * @param provider the provider to filter by
     * @return list of representators for the provider
     */
    List<ProviderRepresentator> findByProvider(Provider provider);

    /**
     * Find provider representators by first name containing (case insensitive)
     * @param firstName the partial first name to search for
     * @return list of representators containing the first name
     */
    @Query("SELECT pr FROM ProviderRepresentator pr WHERE LOWER(pr.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    List<ProviderRepresentator> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    /**
     * Find provider representators by last name containing (case insensitive)
     * @param lastName the partial last name to search for
     * @return list of representators containing the last name
     */
    @Query("SELECT pr FROM ProviderRepresentator pr WHERE LOWER(pr.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<ProviderRepresentator> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName);

    /**
     * Find provider representators by full name (case insensitive)
     * @param firstName the first name to search for
     * @param lastName the last name to search for
     * @return list of representators with matching full name
     */
    @Query("SELECT pr FROM ProviderRepresentator pr WHERE LOWER(pr.firstName) = LOWER(:firstName) AND LOWER(pr.lastName) = LOWER(:lastName)")
    List<ProviderRepresentator> findByFullNameIgnoreCase(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * Find provider representators by phone number
     * @param phoneNumber the phone number to search for
     * @return list of representators with matching phone number
     */
    List<ProviderRepresentator> findByPhoneNumber(String phoneNumber);

    /**
     * Find provider representators by email (case insensitive)
     * @param email the email to search for
     * @return list of representators with matching email
     */
    @Query("SELECT pr FROM ProviderRepresentator pr WHERE LOWER(pr.email) = LOWER(:email)")
    List<ProviderRepresentator> findByEmailIgnoreCase(@Param("email") String email);

    /**
     * Find provider representators by position containing (case insensitive)
     * @param position the partial position to search for
     * @return list of representators containing the position
     */
    @Query("SELECT pr FROM ProviderRepresentator pr WHERE LOWER(pr.position) LIKE LOWER(CONCAT('%', :position, '%'))")
    List<ProviderRepresentator> findByPositionContainingIgnoreCase(@Param("position") String position);

    /**
     * Find provider representators by provider ordered by last name
     * @param provider the provider to filter by
     * @return list of representators ordered by last name ascending
     */
    List<ProviderRepresentator> findByProviderOrderByLastNameAsc(Provider provider);

    /**
     * Count representators by provider
     * @param provider the provider to count representators for
     * @return count of representators for the provider
     */
    long countByProvider(Provider provider);

    /**
     * Find primary representator for provider (if any)
     * @param provider the provider to find primary representator for
     * @return optional primary representator for the provider
     */
    @Query("SELECT pr FROM ProviderRepresentator pr WHERE pr.provider = :provider AND pr.primary = true")
    Optional<ProviderRepresentator> findPrimaryRepresentatorByProvider(@Param("provider") Provider provider);

    /**
     * Check if email exists for any representator
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    @Query("SELECT COUNT(pr) > 0 FROM ProviderRepresentator pr WHERE LOWER(pr.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    /**
     * Check if phone number exists for any representator
     * @param phoneNumber the phone number to check
     * @return true if phone number exists, false otherwise
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Delete representators by provider
     * @param provider the provider to delete representators for
     */
    void deleteByProvider(Provider provider);
}