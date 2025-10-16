/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EmployeeDTO
 *	@CreatedOn	: 10-16-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Employee Data Transfer Object
 * Maps exactly to Employee model fields: F_00=id, F_01=serial, F_02=hiringDate, F_03=person, F_04=militaryRank, F_05=job
 * F_03 (person) is required foreign key
 * F_04 (militaryRank) is required foreign key
 * F_05 (job) is optional foreign key
 * F_01 (serial) and F_02 (hiringDate) are optional
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO {

    private Long id; // F_00

    private String serial; // F_01 - optional

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date hiringDate; // F_02 - optional

    @NotNull(message = "Person is required")
    private Long personId; // F_03 - required foreign key

    @NotNull(message = "Military rank is required")
    private Long militaryRankId; // F_04 - required foreign key

    private Long jobId; // F_05 - optional foreign key

    // Nested information for display purposes - using existing DTO classes
    private PersonDTO person;
    private MilitaryRankDTO militaryRank;
    private JobDTO job;

    /**
     * Create DTO from entity
     */
    public static EmployeeDTO fromEntity(dz.mdn.raas.common.administration.model.Employee employee) {
        if (employee == null) return null;
        
        PersonDTO personDTO = null;
        if (employee.getPerson() != null) {
            personDTO = PersonDTO.fromEntity(employee.getPerson());
        }
        
        MilitaryRankDTO militaryRankDTO = null;
        if (employee.getMilitaryRank() != null) {
            militaryRankDTO = MilitaryRankDTO.fromEntity(employee.getMilitaryRank());
        }
        
        JobDTO jobDTO = null;
        if (employee.getJob() != null) {
            jobDTO = JobDTO.fromEntity(employee.getJob());
        }
        
        return EmployeeDTO.builder()
                .id(employee.getId())
                .serial(employee.getSerial())
                .hiringDate(employee.getHiringDate())
                .personId(employee.getPerson() != null ? employee.getPerson().getId() : null)
                .militaryRankId(employee.getMilitaryRank() != null ? employee.getMilitaryRank().getId() : null)
                .jobId(employee.getJob() != null ? employee.getJob().getId() : null)
                .person(personDTO)
                .militaryRank(militaryRankDTO)
                .job(jobDTO)
                .build();
    }

    /**
     * Convert to entity (without setting foreign key relationships - use service for that)
     */
    public dz.mdn.raas.common.administration.model.Employee toEntity() {
        dz.mdn.raas.common.administration.model.Employee employee = 
            new dz.mdn.raas.common.administration.model.Employee();
        employee.setId(this.id);
        employee.setSerial(this.serial);
        employee.setHiringDate(this.hiringDate);
        // Note: person, militaryRank, and job should be set by the service layer
        return employee;
    }

    /**
     * Update entity from DTO (without updating foreign key relationships - use service for that)
     */
    public void updateEntity(dz.mdn.raas.common.administration.model.Employee employee) {
        if (this.serial != null) {
            employee.setSerial(this.serial);
        }
        if (this.hiringDate != null) {
            employee.setHiringDate(this.hiringDate);
        }
        // Note: person, militaryRank, and job should be updated by the service layer
    }

    /**
     * Get employee display name (from person)
     */
    public String getDisplayName() {
        return person != null ? person.getDisplayName() : "N/A";
    }

    /**
     * Get employee full name in Arabic (from person)
     */
    public String getFullNameAr() {
        return person != null ? person.getFullNameAr() : null;
    }

    /**
     * Get employee full name in Latin (from person)
     */
    public String getFullNameLt() {
        return person != null ? person.getFullNameLt() : null;
    }

    /**
     * Get military rank designation (from militaryRank)
     */
    public String getMilitaryRankDesignation() {
        return militaryRank != null ? militaryRank.getDesignationFr() : null;
    }

    /**
     * Get military rank abbreviation (from militaryRank)
     */
    public String getMilitaryRankAbbreviation() {
        return militaryRank != null ? militaryRank.getAbbreviationFr() : null;
    }

    /**
     * Get job designation (from job)
     */
    public String getJobDesignation() {
        return job != null ? job.getDesignationFr() : null;
    }

    /**
     * Get job structure (from job)
     */
    public String getJobStructure() {
        return job != null ? job.getStructureDesignation() : null;
    }

    /**
     * Calculate years of service
     */
    public Integer getYearsOfService() {
        if (hiringDate == null) return null;
        
        java.util.Calendar hireCal = java.util.Calendar.getInstance();
        hireCal.setTime(hiringDate);
        
        java.util.Calendar now = java.util.Calendar.getInstance();
        
        int years = now.get(java.util.Calendar.YEAR) - hireCal.get(java.util.Calendar.YEAR);
        
        if (now.get(java.util.Calendar.DAY_OF_YEAR) < hireCal.get(java.util.Calendar.DAY_OF_YEAR)) {
            years--;
        }
        
        return years;
    }

    /**
     * Get hiring year
     */
    public Integer getHiringYear() {
        if (hiringDate == null) return null;
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(hiringDate);
        return cal.get(java.util.Calendar.YEAR);
    }

    /**
     * Get service category based on years of service
     */
    public String getServiceCategory() {
        Integer years = getYearsOfService();
        if (years == null) return "UNKNOWN";
        
        if (years < 2) return "NEW_RECRUIT";
        if (years < 5) return "JUNIOR";
        if (years < 10) return "EXPERIENCED";
        if (years < 20) return "SENIOR";
        if (years < 30) return "VETERAN";
        return "DISTINGUISHED_VETERAN";
    }

    /**
     * Check if employee is eligible for retirement
     */
    public boolean isEligibleForRetirement() {
        Integer years = getYearsOfService();
        Integer age = person != null ? person.getAge() : null;
        
        // Basic retirement eligibility: 30 years of service OR age 60+
        return (years != null && years >= 30) || (age != null && age >= 60);
    }

    /**
     * Get employee status based on service
     */
    public String getEmployeeStatus() {
        if (isEligibleForRetirement()) return "RETIREMENT_ELIGIBLE";
        
        Integer years = getYearsOfService();
        if (years == null) return "UNKNOWN";
        
        if (years < 1) return "PROBATIONARY";
        if (years < 5) return "ACTIVE_JUNIOR";
        if (years < 15) return "ACTIVE_SENIOR";
        return "ACTIVE_VETERAN";
    }

    /**
     * Get promotion eligibility
     */
    public String getPromotionEligibility() {
        Integer years = getYearsOfService();
        if (years == null) return "UNKNOWN";
        
        String rankCategory = militaryRank != null ? militaryRank.getRankCategory() : null;
        
        if (rankCategory != null) {
            return switch (rankCategory) {
                case "ENLISTED" -> years >= 2 ? "ELIGIBLE" : "NOT_ELIGIBLE";
                case "NCO" -> years >= 4 ? "ELIGIBLE" : "NOT_ELIGIBLE";
                case "OFFICER" -> years >= 3 ? "ELIGIBLE" : "NOT_ELIGIBLE";
                case "SENIOR_OFFICER" -> years >= 5 ? "ELIGIBLE" : "NOT_ELIGIBLE";
                default -> "REVIEW_REQUIRED";
            };
        }
        
        return years >= 3 ? "ELIGIBLE" : "NOT_ELIGIBLE";
    }

    /**
     * Check if employee has complete profile
     */
    public boolean hasCompleteProfile() {
        return personId != null && militaryRankId != null && 
               serial != null && !serial.trim().isEmpty() &&
               hiringDate != null && jobId != null;
    }

    /**
     * Get profile completeness percentage
     */
    public double getProfileCompleteness() {
        int totalFields = 5; // Total required/important fields
        int filledFields = 0;
        
        if (personId != null) filledFields++;
        if (militaryRankId != null) filledFields++;
        if (serial != null && !serial.trim().isEmpty()) filledFields++;
        if (hiringDate != null) filledFields++;
        if (jobId != null) filledFields++;
        
        return (double) filledFields / totalFields * 100;
    }

    /**
     * Get employee classification
     */
    public String getEmployeeClassification() {
        if (militaryRank != null) {
            String rankCategory = militaryRank.getRankCategory();
            String jobCategory = job != null ? job.getJobCategory() : null;
            
            if (rankCategory != null && jobCategory != null) {
                return rankCategory + "_" + jobCategory;
            }
            return rankCategory != null ? rankCategory : "UNKNOWN";
        }
        return "UNCLASSIFIED";
    }

    /**
     * Get command authority level
     */
    public String getCommandAuthority() {
        if (militaryRank != null) {
            String rankCategory = militaryRank.getRankCategory();
            Integer years = getYearsOfService();
            
            if ("SENIOR_OFFICER".equals(rankCategory)) return "HIGH_COMMAND";
            if ("OFFICER".equals(rankCategory) && years != null && years >= 5) return "UNIT_COMMAND";
            if ("NCO".equals(rankCategory) && years != null && years >= 8) return "SQUAD_LEADERSHIP";
            if (years != null && years >= 10) return "EXPERIENCED_LEADERSHIP";
            
            return "INDIVIDUAL_CONTRIBUTOR";
        }
        return "UNKNOWN";
    }

    /**
     * Create simplified DTO for dropdowns
     */
    public static EmployeeDTO createSimple(Long id, String serial, Long personId, Long militaryRankId) {
        return EmployeeDTO.builder()
                .id(id)
                .serial(serial)
                .personId(personId)
                .militaryRankId(militaryRankId)
                .build();
    }

    /**
     * Validate required fields are present
     */
    public boolean isValid() {
        return personId != null && militaryRankId != null;
    }

    /**
     * Get short display for lists
     */
    public String getShortDisplay() {
        StringBuilder sb = new StringBuilder();
        
        if (militaryRank != null && militaryRank.getAbbreviationFr() != null) {
            sb.append(militaryRank.getAbbreviationFr()).append(" ");
        }
        
        String name = getDisplayName();
        if (name.length() > 25) {
            sb.append(name.substring(0, 25)).append("...");
        } else {
            sb.append(name);
        }
        
        if (serial != null && !serial.trim().isEmpty()) {
            sb.append(" (").append(serial).append(")");
        }
        
        return sb.toString();
    }

    /**
     * Get full display with all available information
     */
    public String getFullDisplay() {
        StringBuilder sb = new StringBuilder();
        
        if (militaryRank != null) {
            sb.append(militaryRank.getDesignationFr()).append(" ");
        }
        
        sb.append(getDisplayName());
        
        if (serial != null && !serial.trim().isEmpty()) {
            sb.append(" - ").append(serial);
        }
        
        if (job != null) {
            sb.append(" - ").append(job.getDesignationFr());
        }
        
        Integer years = getYearsOfService();
        if (years != null) {
            sb.append(" (").append(years).append(" années de service)");
        }
        
        return sb.toString();
    }

    /**
     * Get comparison key for sorting (by rank level, then name)
     */
    public String getComparisonKey() {
        StringBuilder key = new StringBuilder();
        
        if (militaryRank != null && militaryRank.getRankLevel() != null) {
            key.append(String.format("%03d", militaryRank.getRankLevel()));
        } else {
            key.append("999");
        }
        
        key.append("_");
        
        String name = getDisplayName();
        key.append(name != null ? name.toLowerCase() : "zzz");
        
        return key.toString();
    }

    /**
     * Get display with rank and name
     */
    public String getDisplayWithRank() {
        StringBuilder sb = new StringBuilder();
        
        if (militaryRank != null && militaryRank.getAbbreviationFr() != null) {
            sb.append(militaryRank.getAbbreviationFr()).append(" ");
        }
        
        sb.append(getDisplayName());
        
        return sb.toString();
    }

    /**
     * Get display with job context
     */
    public String getDisplayWithJob() {
        StringBuilder sb = new StringBuilder(getDisplayName());
        
        if (job != null) {
            sb.append(" - ").append(job.getDesignationFr());
            
            String structure = job.getStructureDesignation();
            if (structure != null) {
                sb.append(" @ ").append(structure);
            }
        }
        
        return sb.toString();
    }

    /**
     * Get formal military display
     */
    public String getFormalMilitaryDisplay() {
        StringBuilder sb = new StringBuilder();
        
        if (militaryRank != null) {
            sb.append(militaryRank.getDesignationFr()).append(" ");
        }
        
        if (person != null) {
            String formalName = person.getFormalDisplayName();
            sb.append(formalName);
        }
        
        if (serial != null && !serial.trim().isEmpty()) {
            sb.append(", ").append(serial);
        }
        
        return sb.toString();
    }

    /**
     * Get service record summary
     */
    public String getServiceRecordSummary() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Service: ");
        
        Integer years = getYearsOfService();
        if (years != null) {
            sb.append(years).append(" ans");
        } else {
            sb.append("N/A");
        }
        
        if (hiringDate != null) {
            sb.append(" (depuis ").append(new java.text.SimpleDateFormat("yyyy").format(hiringDate)).append(")");
        }
        
        sb.append(" - Statut: ").append(getEmployeeStatus());
        
        return sb.toString();
    }

    /**
     * Check if employee needs profile update
     */
    public boolean needsProfileUpdate() {
        double completeness = getProfileCompleteness();
        return completeness < 80.0;
    }

    /**
     * Get retirement eligibility years
     */
    public Integer getYearsToRetirement() {
        Integer years = getYearsOfService();
        Integer age = person != null ? person.getAge() : null;
        
        if (years == null && age == null) return null;
        
        int yearsToRetirementByService = years != null ? Math.max(0, 30 - years) : 30;
        int yearsToRetirementByAge = age != null ? Math.max(0, 60 - age) : 60;
        
        return Math.min(yearsToRetirementByService, yearsToRetirementByAge);
    }

    /**
     * Get next promotion timeline estimate
     */
    public String getNextPromotionTimeline() {
        Integer years = getYearsOfService();
        String eligibility = getPromotionEligibility();
        
        if ("ELIGIBLE".equals(eligibility)) {
            return "ELIGIBLE_NOW";
        }
        
        if (years == null) return "UNKNOWN";
        
        String rankCategory = militaryRank != null ? militaryRank.getMilitaryCategory().getDesignationFr() : null;
        if (rankCategory != null) {
            return switch (rankCategory) {
                case "ENLISTED" -> years < 2 ? (2 - years) + "_YEARS" : "REVIEW_REQUIRED";
                case "NCO" -> years < 4 ? (4 - years) + "_YEARS" : "REVIEW_REQUIRED";
                case "OFFICER" -> years < 3 ? (3 - years) + "_YEARS" : "REVIEW_REQUIRED";
                case "SENIOR_OFFICER" -> years < 5 ? (5 - years) + "_YEARS" : "REVIEW_REQUIRED";
                default -> "REVIEW_REQUIRED";
            };
        }
        
        return years < 3 ? (3 - years) + "_YEARS" : "REVIEW_REQUIRED";
    }

    /**
     * Get comprehensive employee summary
     */
    public String getEmployeeSummary() {
        return getFormalMilitaryDisplay() + " | " + 
               getServiceRecordSummary() + " | " + 
               "Poste: " + (job != null ? job.getDesignationFr() : "Non assigné");
    }
}
