package dz.mdn.raas.bussiness.provider.repository;

import dz.mdn.raas.bussiness.provider.model.EconomicDomain;
import dz.mdn.raas.bussiness.provider.model.EconomicNature;
import dz.mdn.raas.bussiness.provider.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Provider entity operations
 * Manages provider data access and complex queries
 */
@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    /**
     * Find provider by tax registration number (NIF)
     * @param taxRegistrationNumber the tax registration number to search for
     * @return optional provider with matching tax registration number
     */
    Optional<Provider> findByTaxRegistrationNumber(String taxRegistrationNumber);

    /**
     * Find provider by commercial register number
     * @param commercialRegisterNumber the commercial register number to search for
     * @return optional provider with matching commercial register number
     */
    Optional<Provider> findByCommercialRegisterNumber(String commercialRegisterNumber);

    /**
     * Find providers by name containing (case insensitive)
     * @param name the partial name to search for
     * @return list of providers containing the name
     */
    @Query("SELECT p FROM Provider p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Provider> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find providers by economic nature
     * @param economicNature the economic nature to filter by
     * @return list of providers with matching economic nature
     */
    List<Provider> findByEconomicNature(EconomicNature economicNature);

    /**
     * Find providers by economic domain
     * @param economicDomain the economic domain to filter by
     * @return list of providers with matching economic domain
     */
    List<Provider> findByEconomicDomain(EconomicDomain economicDomain);

    /**
     * Find providers by address containing (case insensitive)
     * @param address the partial address to search for
     * @return list of providers with matching address
     */
    @Query("SELECT p FROM Provider p WHERE LOWER(p.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Provider> findByAddressContainingIgnoreCase(@Param("address") String address);

    /**
     * Find providers by city (case insensitive)
     * @param city the city to search for
     * @return list of providers in the specified city
     */
    @Query("SELECT p FROM Provider p WHERE LOWER(p.city) = LOWER(:city)")
    List<Provider> findByCityIgnoreCase(@Param("city") String city);

    /**
     * Find providers by phone number
     * @param phoneNumber the phone number to search for
     * @return list of providers with matching phone number
     */
    List<Provider> findByPhoneNumber(String phoneNumber);

    /**
     * Find providers by email (case insensitive)
     * @param email the email to search for
     * @return list of providers with matching email
     */
    @Query("SELECT p FROM Provider p WHERE LOWER(p.email) = LOWER(:email)")
    List<Provider> findByEmailIgnoreCase(@Param("email") String email);

    /**
     * Find all active providers
     * @return list of active providers
     */
    @Query("SELECT p FROM Provider p WHERE p.active = true")
    List<Provider> findAllActive();

    /**
     * Find providers ordered by name
     * @return list of providers ordered by name ascending
     */
    List<Provider> findAllByOrderByNameAsc();

    /**
     * Find providers by economic nature and domain
     * @param economicNature the economic nature to filter by
     * @param economicDomain the economic domain to filter by
     * @return list of providers matching both criteria
     */
    List<Provider> findByEconomicNatureAndEconomicDomain(EconomicNature economicNature, 
                                                         EconomicDomain economicDomain);

    /**
     * Check if provider exists by tax registration number
     * @param taxRegistrationNumber the tax registration number to check
     * @return true if exists, false otherwise
     */
    boolean existsByTaxRegistrationNumber(String taxRegistrationNumber);

    /**
     * Check if provider exists by commercial register number
     * @param commercialRegisterNumber the commercial register number to check
     * @return true if exists, false otherwise
     */
    boolean existsByCommercialRegisterNumber(String commercialRegisterNumber);

    /**
     * Find providers with pagination
     * @param pageable pagination information
     * @return paginated list of providers
     */
    Page<Provider> findAllByOrderByNameAsc(Pageable pageable);

    /**
     * Count active providers
     * @return number of active providers
     */
    @Query("SELECT COUNT(p) FROM Provider p WHERE p.active = true")
    long countActive();

    /**
     * Count providers by economic nature
     * @param economicNature the economic nature to count
     * @return number of providers with the specified economic nature
     */
    long countByEconomicNature(EconomicNature economicNature);

    /**
     * Find providers by city with pagination
     * @param city the city to filter by
     * @param pageable pagination information
     * @return paginated list of providers in the specified city
     */
    @Query("SELECT p FROM Provider p WHERE LOWER(p.city) = LOWER(:city)")
    Page<Provider> findByCityIgnoreCase(@Param("city") String city, Pageable pageable);
}