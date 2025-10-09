package dz.mdn.raas.common.administration.repository;

import dz.mdn.raas.common.administration.model.Locality;
import dz.mdn.raas.common.administration.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Person entity operations
 * Manages person data access and queries
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Find person by first name and last name
     * @param firstName the first name to search for
     * @param lastName the last name to search for
     * @return list of persons with matching full name
     */
    List<Person> findByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Find persons by first name containing (case insensitive)
     * @param firstName the partial first name to search for
     * @return list of persons containing the first name
     */
    @Query("SELECT p FROM Person p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    List<Person> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    /**
     * Find persons by last name containing (case insensitive)
     * @param lastName the partial last name to search for
     * @return list of persons containing the last name
     */
    @Query("SELECT p FROM Person p WHERE LOWER(p.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    List<Person> findByLastNameContainingIgnoreCase(@Param("lastName") String lastName);

    /**
     * Find persons by birth locality
     * @param birthPlace the birth locality to filter by
     * @return list of persons born in the locality
     */
    List<Person> findByBirthPlace(Locality birthPlace);

    /**
     * Find persons by birth date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of persons born within the date range
     */
    @Query("SELECT p FROM Person p WHERE p.birthDate BETWEEN :startDate AND :endDate")
    List<Person> findByBirthDateBetween(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate);

    /**
     * Find persons by birth date
     * @param birthDate the birth date to search for
     * @return list of persons with matching birth date
     */
    List<Person> findByBirthDate(LocalDate birthDate);

    /**
     * Find persons by age range
     * @param minAge minimum age
     * @param maxAge maximum age
     * @return list of persons within the age range
     */
    @Query("SELECT p FROM Person p WHERE YEAR(CURRENT_DATE) - YEAR(p.birthDate) BETWEEN :minAge AND :maxAge")
    List<Person> findByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);

    /**
     * Find persons ordered by last name then first name
     * @return list of persons ordered by last name, first name ascending
     */
    List<Person> findAllByOrderByLastNameAscFirstNameAsc();

    /**
     * Find persons by full name (case insensitive)
     * @param firstName the first name to search for
     * @param lastName the last name to search for
     * @return list of persons with matching full name
     */
    @Query("SELECT p FROM Person p WHERE LOWER(p.firstName) = LOWER(:firstName) AND LOWER(p.lastName) = LOWER(:lastName)")
    List<Person> findByFullNameIgnoreCase(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * Search persons by name (first or last name containing)
     * @param searchTerm the search term to match against first or last name
     * @return list of persons matching the search term
     */
    @Query("SELECT p FROM Person p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Person> searchByName(@Param("searchTerm") String searchTerm);

    /**
     * Find persons born in current year
     * @return list of persons born this year
     */
    @Query("SELECT p FROM Person p WHERE YEAR(p.birthDate) = YEAR(CURRENT_DATE)")
    List<Person> findBornThisYear();

    /**
     * Find persons born after specified date
     * @param date the date to compare against
     * @return list of persons born after the date
     */
    List<Person> findByBirthDateAfter(LocalDate date);

    /**
     * Find persons born before specified date
     * @param date the date to compare against
     * @return list of persons born before the date
     */
    List<Person> findByBirthDateBefore(LocalDate date);

    /**
     * Count persons by birth locality
     * @param birthPlace the birth locality to count for
     * @return count of persons born in the locality
     */
    long countByBirthPlace(Locality birthPlace);

    /**
     * Find persons by birth locality ordered by birth date
     * @param birthPlace the birth locality to filter by
     * @return list of persons ordered by birth date ascending
     */
    List<Person> findByBirthPlaceOrderByBirthDateAsc(Locality birthPlace);
}