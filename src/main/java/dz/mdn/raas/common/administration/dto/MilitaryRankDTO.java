/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryRankDTO
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
 * MilitaryRank Data Transfer Object
 * Maps exactly to MilitaryRank model fields: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=militaryCategory
 * F_03 (designationFr) has unique constraint and is required
 * F_01 (designationAr) and F_02 (designationEn) are optional
 * F_04 (militaryCategory) is required foreign key
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MilitaryRankDTO {

    private Long id; // F_00

    @Size(max = 50, message = "Arabic designation must not exceed 50 characters")
    private String designationAr; // F_01 - optional

    @Size(max = 50, message = "English designation must not exceed 50 characters")
    private String designationEn; // F_02 - optional

    @NotBlank(message = "French designation is required")
    @Size(max = 50, message = "French designation must not exceed 50 characters")
    private String designationFr; // F_03 - required and unique

    @NotNull(message = "Military category is required")
    private Long militaryCategoryId; // F_04 - required foreign key

    // Nested military category information for display purposes
    private MilitaryCategoryInfo militaryCategory;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MilitaryCategoryInfo {
        private Long id;
        private String designationFr;
        private String designationEn;
        private String designationAr;
    }

    /**
     * Create DTO from entity
     */
    public static MilitaryRankDTO fromEntity(dz.mdn.raas.common.administration.model.MilitaryRank militaryRank) {
        if (militaryRank == null) return null;
        
        MilitaryCategoryInfo categoryInfo = null;
        if (militaryRank.getMilitaryCategory() != null) {
            categoryInfo = MilitaryCategoryInfo.builder()
                    .id(militaryRank.getMilitaryCategory().getId())
                    .designationFr(militaryRank.getMilitaryCategory().getDesignationFr())
                    .designationEn(militaryRank.getMilitaryCategory().getDesignationEn())
                    .designationAr(militaryRank.getMilitaryCategory().getDesignationAr())
                    .build();
        }
        
        return MilitaryRankDTO.builder()
                .id(militaryRank.getId())
                .designationAr(militaryRank.getDesignationAr())
                .designationEn(militaryRank.getDesignationEn())
                .designationFr(militaryRank.getDesignationFr())
                .militaryCategoryId(militaryRank.getMilitaryCategory() != null ? 
                                  militaryRank.getMilitaryCategory().getId() : null)
                .militaryCategory(categoryInfo)
                .build();
    }

    /**
     * Convert to entity (without setting the MilitaryCategory - use service for that)
     */
    public dz.mdn.raas.common.administration.model.MilitaryRank toEntity() {
        dz.mdn.raas.common.administration.model.MilitaryRank militaryRank = 
            new dz.mdn.raas.common.administration.model.MilitaryRank();
        militaryRank.setId(this.id);
        militaryRank.setDesignationAr(this.designationAr);
        militaryRank.setDesignationEn(this.designationEn);
        militaryRank.setDesignationFr(this.designationFr);
        // Note: militaryCategory should be set by the service layer
        return militaryRank;
    }

    /**
     * Update entity from DTO (without updating MilitaryCategory - use service for that)
     */
    public void updateEntity(dz.mdn.raas.common.administration.model.MilitaryRank militaryRank) {
        if (this.designationAr != null) {
            militaryRank.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            militaryRank.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            militaryRank.setDesignationFr(this.designationFr);
        }
        // Note: militaryCategory should be updated by the service layer
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
     * Check if military rank has multiple language support
     */
    public boolean isMultilingual() {
        int languageCount = 0;
        if (designationAr != null && !designationAr.trim().isEmpty()) languageCount++;
        if (designationEn != null && !designationEn.trim().isEmpty()) languageCount++;
        if (designationFr != null && !designationFr.trim().isEmpty()) languageCount++;
        return languageCount > 1;
    }

    /**
     * Get available languages for this military rank
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
     * Get rank level based on French designation analysis
     */
    public String getRankLevel() {
        if (designationFr == null) return "UNKNOWN";
        
        String designation = designationFr.toLowerCase();
        
        // General officers (highest level)
        if (designation.contains("général") || designation.contains("amiral") || 
            designation.contains("general") || designation.contains("admiral")) {
            return "GENERAL_OFFICER";
        }
        
        // Field officers
        if (designation.contains("colonel") || designation.contains("lieutenant-colonel") || 
            designation.contains("commandant") || designation.contains("major")) {
            return "FIELD_OFFICER";
        }
        
        // Company officers
        if (designation.contains("capitaine") || designation.contains("lieutenant") || 
            designation.contains("sous-lieutenant") || designation.contains("enseigne")) {
            return "COMPANY_OFFICER";
        }
        
        // Senior NCOs
        if (designation.contains("adjudant-chef") || designation.contains("adjudant") || 
            designation.contains("sergent-chef") || designation.contains("maître")) {
            return "SENIOR_NCO";
        }
        
        // Junior NCOs
        if (designation.contains("sergent") || designation.contains("caporal-chef") || 
            designation.contains("quartier-maître")) {
            return "JUNIOR_NCO";
        }
        
        // Enlisted ranks
        if (designation.contains("caporal") || designation.contains("soldat") || 
            designation.contains("matelot") || designation.contains("aviateur")) {
            return "ENLISTED";
        }
        
        // Cadet/Student ranks
        if (designation.contains("élève") || designation.contains("aspirant") || 
            designation.contains("cadet")) {
            return "CADET";
        }
        
        return "OTHER";
    }

    /**
     * Get rank hierarchy order (lower number = higher rank)
     */
    public int getRankOrder() {
        return switch (getRankLevel()) {
            case "GENERAL_OFFICER" -> 1;
            case "FIELD_OFFICER" -> 2;
            case "COMPANY_OFFICER" -> 3;
            case "SENIOR_NCO" -> 4;
            case "JUNIOR_NCO" -> 5;
            case "ENLISTED" -> 6;
            case "CADET" -> 7;
            default -> 8;
        };
    }

    /**
     * Check if this is an officer rank
     */
    public boolean isOfficerRank() {
        String level = getRankLevel();
        return "GENERAL_OFFICER".equals(level) || "FIELD_OFFICER".equals(level) || 
               "COMPANY_OFFICER".equals(level);
    }

    /**
     * Check if this is an NCO rank
     */
    public boolean isNCORank() {
        String level = getRankLevel();
        return "SENIOR_NCO".equals(level) || "JUNIOR_NCO".equals(level);
    }

    /**
     * Check if this is an enlisted rank
     */
    public boolean isEnlistedRank() {
        return "ENLISTED".equals(getRankLevel());
    }

    /**
     * Get promotion eligibility
     */
    public boolean isPromotionEligible() {
        // Generally, all ranks except the highest can be promoted
        String level = getRankLevel();
        return !"GENERAL_OFFICER".equals(level) || 
               (designationFr != null && !designationFr.toLowerCase().contains("général d'armée"));
    }

    /**
     * Get command level
     */
    public String getCommandLevel() {
        return switch (getRankLevel()) {
            case "GENERAL_OFFICER" -> "STRATEGIC";
            case "FIELD_OFFICER" -> "OPERATIONAL";
            case "COMPANY_OFFICER" -> "TACTICAL";
            case "SENIOR_NCO" -> "SQUAD_TEAM";
            case "JUNIOR_NCO" -> "TEAM";
            case "ENLISTED" -> "INDIVIDUAL";
            case "CADET" -> "TRAINEE";
            default -> "NONE";
        };
    }

    /**
     * Get military category designation if available
     */
    public String getMilitaryCategoryDesignation() {
        return militaryCategory != null ? militaryCategory.getDesignationFr() : null;
    }

    /**
     * Get seniority within rank level (estimated based on designation)
     */
    public int getSeniorityLevel() {
        if (designationFr == null) return 0;
        
        String designation = designationFr.toLowerCase();
        
        // General officers
        if (designation.contains("général d'armée") || designation.contains("amiral")) return 5;
        if (designation.contains("général de corps d'armée")) return 4;
        if (designation.contains("général de division")) return 3;
        if (designation.contains("général de brigade")) return 2;
        
        // Field officers
        if (designation.contains("colonel")) return 3;
        if (designation.contains("lieutenant-colonel")) return 2;
        if (designation.contains("commandant") || designation.contains("major")) return 1;
        
        // Company officers
        if (designation.contains("capitaine")) return 3;
        if (designation.contains("lieutenant")) return 2;
        if (designation.contains("sous-lieutenant")) return 1;
        
        return 1; // Default seniority
    }

    /**
     * Get insignia description
     */
    public String getInsigniaDescription() {
        String level = getRankLevel();
        int seniority = getSeniorityLevel();
        
        return switch (level) {
            case "GENERAL_OFFICER" -> seniority + " étoiles";
            case "FIELD_OFFICER" -> "Galons de " + designationFr;
            case "COMPANY_OFFICER" -> "Barrettes de " + designationFr;
            case "SENIOR_NCO" -> "Chevrons dorés";
            case "JUNIOR_NCO" -> "Chevrons argentés";
            case "ENLISTED" -> "Pas d'insigne ou chevrons simples";
            case "CADET" -> "Insignes d'élève";
            default -> "Insigne spécialisé";
        };
    }

    /**
     * Create simplified DTO for dropdowns
     */
    public static MilitaryRankDTO createSimple(Long id, String designationFr, Long militaryCategoryId) {
        return MilitaryRankDTO.builder()
                .id(id)
                .designationFr(designationFr)
                .militaryCategoryId(militaryCategoryId)
                .build();
    }

    /**
     * Validate required fields are present
     */
    public boolean isValid() {
        return designationFr != null && !designationFr.trim().isEmpty() && 
               militaryCategoryId != null;
    }

    /**
     * Get short display for lists
     */
    public String getShortDisplay() {
        return designationFr != null && designationFr.length() > 20 ? 
                designationFr.substring(0, 20) + "..." : designationFr;
    }

    /**
     * Get full display with all languages and category
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
        
        if (militaryCategory != null && militaryCategory.getDesignationFr() != null) {
            sb.append(" (").append(militaryCategory.getDesignationFr()).append(")");
        }
        
        return sb.toString();
    }

    /**
     * Get comparison key for sorting (by rank order then French designation)
     */
    public String getComparisonKey() {
        return String.format("%02d_%s", getRankOrder(), 
                           designationFr != null ? designationFr.toLowerCase() : "");
    }

    /**
     * Get display with rank level
     */
    public String getDisplayWithLevel() {
        return designationFr + " (" + getRankLevel().replace("_", " ").toLowerCase() + ")";
    }

    /**
     * Get rank abbreviation
     */
    public String getRankAbbreviation() {
        if (designationFr == null) return "";
        
        String designation = designationFr.toLowerCase();
        
        // Common French military abbreviations
        if (designation.contains("général")) return "Gén";
        if (designation.contains("colonel")) return "Col";
        if (designation.contains("lieutenant-colonel")) return "Lcl";
        if (designation.contains("commandant")) return "Cdt";
        if (designation.contains("capitaine")) return "Cne";
        if (designation.contains("lieutenant")) return "Lt";
        if (designation.contains("sous-lieutenant")) return "Slt";
        if (designation.contains("adjudant-chef")) return "Adj-C";
        if (designation.contains("adjudant")) return "Adj";
        if (designation.contains("sergent-chef")) return "Sgt-C";
        if (designation.contains("sergent")) return "Sgt";
        if (designation.contains("caporal-chef")) return "Cpl-C";
        if (designation.contains("caporal")) return "Cpl";
        if (designation.contains("soldat")) return "Sdt";
        
        // Default: take first 3 characters
        return designationFr.length() >= 3 ? designationFr.substring(0, 3).toUpperCase() : designationFr.toUpperCase();
    }

    /**
     * Check if rank requires security clearance
     */
    public boolean requiresSecurityClearance() {
        return isOfficerRank() || "SENIOR_NCO".equals(getRankLevel());
    }

    /**
     * Get typical service years for this rank
     */
    public String getTypicalServiceYears() {
        return switch (getRankLevel()) {
            case "GENERAL_OFFICER" -> "25+ years";
            case "FIELD_OFFICER" -> "15-25 years";
            case "COMPANY_OFFICER" -> "5-15 years";
            case "SENIOR_NCO" -> "10-20 years";
            case "JUNIOR_NCO" -> "3-10 years";
            case "ENLISTED" -> "0-5 years";
            case "CADET" -> "Training period";
            default -> "Variable";
        };
    }

    /**
     * Get pay grade equivalent (NATO standard)
     */
    public String getPayGrade() {
        String level = getRankLevel();
        int seniority = getSeniorityLevel();
        
        return switch (level) {
            case "GENERAL_OFFICER" -> "OF-" + (6 + seniority);
            case "FIELD_OFFICER" -> "OF-" + (2 + seniority);
            case "COMPANY_OFFICER" -> "OF-" + seniority;
            case "SENIOR_NCO" -> "OR-" + (6 + seniority);
            case "JUNIOR_NCO" -> "OR-" + (3 + seniority);
            case "ENLISTED" -> "OR-" + (1 + seniority);
            default -> "N/A";
        };
    }
}
