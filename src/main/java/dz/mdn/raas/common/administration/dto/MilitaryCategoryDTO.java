/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryCategoryDTO
 *	@CreatedOn	: 10-16-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.raas.common.administration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MilitaryCategory Data Transfer Object
 * Maps exactly to MilitaryCategory model fields: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr
 * F_03 (designationFr) has unique constraint and is required
 * F_01 (designationAr) and F_02 (designationEn) are optional
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MilitaryCategoryDTO {

    private Long id; // F_00

    @Size(max = 50, message = "Arabic designation must not exceed 50 characters")
    private String designationAr; // F_01 - optional

    @Size(max = 50, message = "English designation must not exceed 50 characters")
    private String designationEn; // F_02 - optional

    @NotBlank(message = "French designation is required")
    @Size(max = 50, message = "French designation must not exceed 50 characters")
    private String designationFr; // F_03 - required and unique

    /**
     * Create DTO from entity
     */
    public static MilitaryCategoryDTO fromEntity(dz.mdn.raas.common.administration.model.MilitaryCategory militaryCategory) {
        if (militaryCategory == null) return null;
        
        return MilitaryCategoryDTO.builder()
                .id(militaryCategory.getId())
                .designationAr(militaryCategory.getDesignationAr())
                .designationEn(militaryCategory.getDesignationEn())
                .designationFr(militaryCategory.getDesignationFr())
                .build();
    }

    /**
     * Convert to entity
     */
    public dz.mdn.raas.common.administration.model.MilitaryCategory toEntity() {
        dz.mdn.raas.common.administration.model.MilitaryCategory militaryCategory = 
            new dz.mdn.raas.common.administration.model.MilitaryCategory();
        militaryCategory.setId(this.id);
        militaryCategory.setDesignationAr(this.designationAr);
        militaryCategory.setDesignationEn(this.designationEn);
        militaryCategory.setDesignationFr(this.designationFr);
        return militaryCategory;
    }

    /**
     * Update entity from DTO
     */
    public void updateEntity(dz.mdn.raas.common.administration.model.MilitaryCategory militaryCategory) {
        if (this.designationAr != null) {
            militaryCategory.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            militaryCategory.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            militaryCategory.setDesignationFr(this.designationFr);
        }
    }

    /**
     * Get default designation (French as it's required)
     */
    public String getDefaultDesignation() {
        return designationFr;
    }

    /**
     * Get designation by language preference
     */
    public String getDesignationByLanguage(String language) {
        if (language == null) return designationFr;
        
        return switch (language.toLowerCase()) {
            case "ar", "arabic" -> designationAr != null ? designationAr : designationFr;
            case "en", "english" -> designationEn != null ? designationEn : designationFr;
            case "fr", "french" -> designationFr;
            default -> designationFr;
        };
    }

    /**
     * Get display text with priority: French designation > English designation > Arabic designation
     */
    public String getDisplayText() {
        if (designationFr != null && !designationFr.trim().isEmpty()) {
            return designationFr;
        }
        if (designationEn != null && !designationEn.trim().isEmpty()) {
            return designationEn;
        }
        if (designationAr != null && !designationAr.trim().isEmpty()) {
            return designationAr;
        }
        return "N/A";
    }

    /**
     * Check if military category has multiple language support
     */
    public boolean isMultilingual() {
        int languageCount = 0;
        if (designationAr != null && !designationAr.trim().isEmpty()) languageCount++;
        if (designationEn != null && !designationEn.trim().isEmpty()) languageCount++;
        if (designationFr != null && !designationFr.trim().isEmpty()) languageCount++;
        return languageCount > 1;
    }

    /**
     * Get available languages for this military category
     */
    public String[] getAvailableLanguages() {
        java.util.List<String> languages = new java.util.ArrayList<>();
        
        if (designationAr != null && !designationAr.trim().isEmpty()) {
            languages.add("arabic");
        }
        if (designationEn != null && !designationEn.trim().isEmpty()) {
            languages.add("english");
        }
        if (designationFr != null && !designationFr.trim().isEmpty()) {
            languages.add("french");
        }
        
        return languages.stream().toArray(String[]::new);
    }

    /**
     * Get military category type based on French designation analysis
     */
    public String getMilitaryCategoryType() {
        if (designationFr == null) return "UNKNOWN";
        
        String designation = designationFr.toLowerCase();
        
        // Officer categories
        if (designation.contains("officier") || designation.contains("officer") || 
            designation.contains("commandant") || designation.contains("colonel")) {
            return "OFFICER";
        }
        
        // Non-commissioned officer categories
        if (designation.contains("sous-officier") || designation.contains("sergent") || 
            designation.contains("adjudant") || designation.contains("nco")) {
            return "NCO";
        }
        
        // Enlisted personnel categories
        if (designation.contains("soldat") || designation.contains("caporal") || 
            designation.contains("enlisted") || designation.contains("militaire du rang")) {
            return "ENLISTED";
        }
        
        // Specialized categories
        if (designation.contains("spécialisé") || designation.contains("specialist") || 
            designation.contains("technicien") || designation.contains("technician")) {
            return "SPECIALIST";
        }
        
        // Medical categories
        if (designation.contains("médical") || designation.contains("medical") || 
            designation.contains("santé") || designation.contains("infirmier")) {
            return "MEDICAL";
        }
        
        // Administrative categories
        if (designation.contains("administratif") || designation.contains("administrative") || 
            designation.contains("civil") || designation.contains("clerical")) {
            return "ADMINISTRATIVE";
        }
        
        // Reserve categories
        if (designation.contains("réserve") || designation.contains("reserve") || 
            designation.contains("auxiliaire") || designation.contains("auxiliary")) {
            return "RESERVE";
        }
        
        // Cadet/Student categories
        if (designation.contains("élève") || designation.contains("étudiant") || 
            designation.contains("cadet") || designation.contains("student")) {
            return "CADET";
        }
        
        // Retired categories
        if (designation.contains("retraité") || designation.contains("retired") || 
            designation.contains("honoraire") || designation.contains("emeritus")) {
            return "RETIRED";
        }
        
        return "GENERAL";
    }

    /**
     * Check if this is an officer category
     */
    public boolean isOfficerCategory() {
        return "OFFICER".equals(getMilitaryCategoryType());
    }

    /**
     * Check if this is an NCO category
     */
    public boolean isNCOCategory() {
        return "NCO".equals(getMilitaryCategoryType());
    }

    /**
     * Check if this is an enlisted category
     */
    public boolean isEnlistedCategory() {
        return "ENLISTED".equals(getMilitaryCategoryType());
    }

    /**
     * Check if this is an active duty category
     */
    public boolean isActiveDuty() {
        String type = getMilitaryCategoryType();
        return "OFFICER".equals(type) || "NCO".equals(type) || "ENLISTED".equals(type) || 
               "SPECIALIST".equals(type) || "MEDICAL".equals(type);
    }

    /**
     * Get military hierarchy level
     */
    public String getHierarchyLevel() {
        return switch (getMilitaryCategoryType()) {
            case "OFFICER" -> "COMMISSIONED";
            case "NCO" -> "NON_COMMISSIONED";
            case "ENLISTED" -> "ENLISTED";
            case "SPECIALIST" -> "TECHNICAL";
            case "MEDICAL" -> "PROFESSIONAL";
            case "ADMINISTRATIVE" -> "CIVILIAN";
            case "RESERVE" -> "RESERVE";
            case "CADET" -> "TRAINEE";
            case "RETIRED" -> "VETERAN";
            default -> "GENERAL";
        };
    }

    /**
     * Get command authority level
     */
    public String getCommandAuthority() {
        return switch (getMilitaryCategoryType()) {
            case "OFFICER" -> "FULL_COMMAND";
            case "NCO" -> "LIMITED_COMMAND";
            case "ENLISTED" -> "NO_COMMAND";
            case "SPECIALIST" -> "TECHNICAL_AUTHORITY";
            case "MEDICAL" -> "PROFESSIONAL_AUTHORITY";
            case "ADMINISTRATIVE" -> "ADMINISTRATIVE_AUTHORITY";
            case "RESERVE" -> "CONDITIONAL_COMMAND";
            case "CADET" -> "NO_COMMAND";
            case "RETIRED" -> "NO_COMMAND";
            default -> "NO_COMMAND";
        };
    }

    /**
     * Get category priority for military hierarchy
     */
    public int getCategoryPriority() {
        return switch (getMilitaryCategoryType()) {
            case "OFFICER" -> 1;
            case "NCO" -> 2;
            case "SPECIALIST" -> 3;
            case "MEDICAL" -> 4;
            case "ENLISTED" -> 5;
            case "ADMINISTRATIVE" -> 6;
            case "CADET" -> 7;
            case "RESERVE" -> 8;
            case "RETIRED" -> 9;
            default -> 10;
        };
    }

    /**
     * Get service branch compatibility
     */
    public String[] getServiceBranches() {
        return switch (getMilitaryCategoryType()) {
            case "OFFICER", "NCO", "ENLISTED" -> new String[]{"ARMY", "NAVY", "AIR_FORCE", "GENDARMERIE"};
            case "SPECIALIST" -> new String[]{"ARMY", "NAVY", "AIR_FORCE"};
            case "MEDICAL" -> new String[]{"ARMY", "NAVY", "AIR_FORCE", "MEDICAL_SERVICE"};
            case "ADMINISTRATIVE" -> new String[]{"ALL_BRANCHES"};
            case "RESERVE" -> new String[]{"ARMY", "NAVY", "AIR_FORCE", "RESERVES"};
            case "CADET" -> new String[]{"MILITARY_ACADEMY"};
            case "RETIRED" -> new String[]{"ALL_BRANCHES"};
            default -> new String[]{"GENERAL"};
        };
    }

    /**
     * Get training requirements
     */
    public String getTrainingRequirement() {
        return switch (getMilitaryCategoryType()) {
            case "OFFICER" -> "MILITARY_ACADEMY";
            case "NCO" -> "NCO_SCHOOL";
            case "ENLISTED" -> "BASIC_TRAINING";
            case "SPECIALIST" -> "TECHNICAL_SCHOOL";
            case "MEDICAL" -> "MEDICAL_TRAINING";
            case "ADMINISTRATIVE" -> "ADMINISTRATIVE_TRAINING";
            case "RESERVE" -> "RESERVE_TRAINING";
            case "CADET" -> "ACADEMY_PROGRAM";
            case "RETIRED" -> "NONE";
            default -> "BASIC_TRAINING";
        };
    }

    /**
     * Check if category requires security clearance
     */
    public boolean requiresSecurityClearance() {
        String type = getMilitaryCategoryType();
        return "OFFICER".equals(type) || "NCO".equals(type) || "SPECIALIST".equals(type);
    }

    /**
     * Get deployment eligibility
     */
    public String getDeploymentEligibility() {
        return switch (getMilitaryCategoryType()) {
            case "OFFICER", "NCO", "ENLISTED" -> "FULL_DEPLOYMENT";
            case "SPECIALIST" -> "TECHNICAL_DEPLOYMENT";
            case "MEDICAL" -> "MEDICAL_DEPLOYMENT";
            case "ADMINISTRATIVE" -> "LIMITED_DEPLOYMENT";
            case "RESERVE" -> "RESERVE_DEPLOYMENT";
            case "CADET" -> "NO_DEPLOYMENT";
            case "RETIRED" -> "NO_DEPLOYMENT";
            default -> "LIMITED_DEPLOYMENT";
        };
    }

    /**
     * Create simplified DTO for dropdowns
     */
    public static MilitaryCategoryDTO createSimple(Long id, String designationFr) {
        return MilitaryCategoryDTO.builder()
                .id(id)
                .designationFr(designationFr)
                .build();
    }

    /**
     * Validate required fields are present
     */
    public boolean isValid() {
        return designationFr != null && !designationFr.trim().isEmpty();
    }

    /**
     * Get short display for lists
     */
    public String getShortDisplay() {
        return designationFr != null && designationFr.length() > 20 ? 
                designationFr.substring(0, 20) + "..." : designationFr;
    }

    /**
     * Get full display with all languages
     */
    public String getFullDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append(designationFr);
        
        if (designationEn != null && !designationEn.equals(designationFr)) {
            sb.append(" / ").append(designationEn);
        }
        
        if (designationAr != null) {
            sb.append(" / ").append(designationAr);
        }
        
        return sb.toString();
    }

    /**
     * Get comparison key for sorting (by French designation)
     */
    public String getComparisonKey() {
        return designationFr != null ? designationFr.toLowerCase() : "";
    }

    /**
     * Get display with category type
     */
    public String getDisplayWithType() {
        return designationFr + " (" + getMilitaryCategoryType().replace("_", " ").toLowerCase() + ")";
    }

    /**
     * Get category abbreviation
     */
    public String getCategoryAbbreviation() {
        return switch (getMilitaryCategoryType()) {
            case "OFFICER" -> "OFF";
            case "NCO" -> "NCO";
            case "ENLISTED" -> "ENL";
            case "SPECIALIST" -> "SPC";
            case "MEDICAL" -> "MED";
            case "ADMINISTRATIVE" -> "ADM";
            case "RESERVE" -> "RES";
            case "CADET" -> "CDT";
            case "RETIRED" -> "RET";
            default -> "GEN";
        };
    }

    /**
     * Check if category has operational role
     */
    public boolean hasOperationalRole() {
        String type = getMilitaryCategoryType();
        return "OFFICER".equals(type) || "NCO".equals(type) || "ENLISTED".equals(type) || 
               "SPECIALIST".equals(type);
    }

    /**
     * Get personnel classification
     */
    public String getPersonnelClassification() {
        return switch (getMilitaryCategoryType()) {
            case "OFFICER" -> "COMMISSIONED_PERSONNEL";
            case "NCO" -> "NON_COMMISSIONED_PERSONNEL";
            case "ENLISTED" -> "ENLISTED_PERSONNEL";
            case "SPECIALIST" -> "TECHNICAL_PERSONNEL";
            case "MEDICAL" -> "MEDICAL_PERSONNEL";
            case "ADMINISTRATIVE" -> "CIVILIAN_PERSONNEL";
            case "RESERVE" -> "RESERVE_PERSONNEL";
            case "CADET" -> "STUDENT_PERSONNEL";
            case "RETIRED" -> "VETERAN_PERSONNEL";
            default -> "GENERAL_PERSONNEL";
        };
    }
}
