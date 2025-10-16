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
 * Military Rank Data Transfer Object
 * Maps exactly to MilitaryRank model fields: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=abbreviationAr, F_05=abbreviationEn, F_06=abbreviationFr, F_07=militaryCategory
 * F_03 (designationFr) has unique constraint and is required
 * F_06 (abbreviationFr) is required
 * F_07 (militaryCategory) is required foreign key
 * F_01, F_02, F_04, F_05 are optional
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

    @Size(max = 10, message = "Arabic abbreviation must not exceed 10 characters")
    private String abbreviationAr; // F_04 - optional

    @Size(max = 10, message = "English abbreviation must not exceed 10 characters")
    private String abbreviationEn; // F_05 - optional

    @NotBlank(message = "French abbreviation is required")
    @Size(max = 10, message = "French abbreviation must not exceed 10 characters")
    private String abbreviationFr; // F_06 - required

    @NotNull(message = "Military category is required")
    private Long militaryCategoryId; // F_07 - required foreign key

    // Nested military category information for display purposes
    private MilitaryCategoryDTO militaryCategory;

    /**
     * Create DTO from entity
     */
    public static MilitaryRankDTO fromEntity(dz.mdn.raas.common.administration.model.MilitaryRank militaryRank) {
        if (militaryRank == null) return null;
        
        MilitaryCategoryDTO militaryCategoryDTO = null;
        if (militaryRank.getMilitaryCategory() != null) {
            militaryCategoryDTO = MilitaryCategoryDTO.fromEntity(militaryRank.getMilitaryCategory());
        }
        
        return MilitaryRankDTO.builder()
                .id(militaryRank.getId())
                .designationAr(militaryRank.getDesignationAr())
                .designationEn(militaryRank.getDesignationEn())
                .designationFr(militaryRank.getDesignationFr())
                .abbreviationAr(militaryRank.getAbbreviationAr())
                .abbreviationEn(militaryRank.getAbbreviationEn())
                .abbreviationFr(militaryRank.getAbbreviationFr())
                .militaryCategoryId(militaryRank.getMilitaryCategory() != null ? militaryRank.getMilitaryCategory().getId() : null)
                .militaryCategory(militaryCategoryDTO)
                .build();
    }

    /**
     * Convert to entity (without setting MilitaryCategory - use service for that)
     */
    public dz.mdn.raas.common.administration.model.MilitaryRank toEntity() {
        dz.mdn.raas.common.administration.model.MilitaryRank militaryRank = 
            new dz.mdn.raas.common.administration.model.MilitaryRank();
        militaryRank.setId(this.id);
        militaryRank.setDesignationAr(this.designationAr);
        militaryRank.setDesignationEn(this.designationEn);
        militaryRank.setDesignationFr(this.designationFr);
        militaryRank.setAbbreviationAr(this.abbreviationAr);
        militaryRank.setAbbreviationEn(this.abbreviationEn);
        militaryRank.setAbbreviationFr(this.abbreviationFr);
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
        if (this.abbreviationAr != null) {
            militaryRank.setAbbreviationAr(this.abbreviationAr);
        }
        if (this.abbreviationEn != null) {
            militaryRank.setAbbreviationEn(this.abbreviationEn);
        }
        if (this.abbreviationFr != null) {
            militaryRank.setAbbreviationFr(this.abbreviationFr);
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
     * Get default abbreviation (French as it's required)
     */
    public String getDefaultAbbreviation() {
        return abbreviationFr;
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
     * Get abbreviation by language preference
     */
    public String getAbbreviationByLanguage(String language) {
        if (language == null) return abbreviationFr;
        
        return switch (language.toLowerCase()) {
            case "ar", "arabic" -> abbreviationAr != null ? abbreviationAr : abbreviationFr;
            case "en", "english" -> abbreviationEn != null ? abbreviationEn : abbreviationFr;
            case "fr", "french" -> abbreviationFr;
            default -> abbreviationFr;
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
     * Get display abbreviation
     */
    public String getDisplayAbbreviation() {
        if (abbreviationFr != null && !abbreviationFr.trim().isEmpty()) {
            return abbreviationFr;
        }
        if (abbreviationEn != null && !abbreviationEn.trim().isEmpty()) {
            return abbreviationEn;
        }
        if (abbreviationAr != null && !abbreviationAr.trim().isEmpty()) {
            return abbreviationAr;
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
    public Integer getRankLevel() {
        if (designationFr == null) return null;
        
        String designation = designationFr.toLowerCase();
        
        // General/Admiral level (highest)
        if (designation.contains("général") || designation.contains("amiral")) {
            if (designation.contains("major")) return 12; // Général-Major
            if (designation.contains("corps")) return 11; // Général de Corps d'Armée
            if (designation.contains("division")) return 10; // Général de Division
            if (designation.contains("brigade")) return 9; // Général de Brigade
            return 10; // Default General
        }
        
        // Colonel level
        if (designation.contains("colonel")) {
            if (designation.contains("lieutenant")) return 7; // Lieutenant-Colonel
            return 8; // Colonel
        }
        
        // Major/Commandant
        if (designation.contains("commandant") || designation.contains("major")) {
            return 6;
        }
        
        // Captain level
        if (designation.contains("capitaine")) {
            return 5;
        }
        
        // Lieutenant level
        if (designation.contains("lieutenant")) {
            if (designation.contains("sous")) return 3; // Sous-Lieutenant
            return 4; // Lieutenant
        }
        
        // Sous-officiers (NCO)
        if (designation.contains("adjudant")) {
            if (designation.contains("chef")) return 2; // Adjudant-Chef
            return 1; // Adjudant
        }
        
        if (designation.contains("sergent")) {
            if (designation.contains("chef")) return 1; // Sergent-Chef
            return 0; // Sergent
        }
        
        if (designation.contains("caporal")) {
            if (designation.contains("chef")) return -1; // Caporal-Chef
            return -2; // Caporal
        }
        
        // Soldats (Enlisted)
        if (designation.contains("soldat")) {
            if (designation.contains("première")) return -3; // Soldat de 1ère Classe
            return -4; // Soldat
        }
        
        return 0; // Default
    }

    /**
     * Get rank category based on designation analysis
     */
    public String getRankCategory() {
        if (designationFr == null) return "UNKNOWN";
        
        String designation = designationFr.toLowerCase();
        
        // Senior Officers
        if (designation.contains("général") || designation.contains("amiral") || 
            designation.contains("colonel")) {
            return "SENIOR_OFFICER";
        }
        
        // Officers
        if (designation.contains("commandant") || designation.contains("capitaine") || 
            designation.contains("lieutenant") || designation.contains("major")) {
            return "OFFICER";
        }
        
        // Non-Commissioned Officers
        if (designation.contains("adjudant") || designation.contains("sergent")) {
            return "NCO";
        }
        
        // Enlisted
        if (designation.contains("caporal") || designation.contains("soldat")) {
            return "ENLISTED";
        }
        
        return "OTHER";
    }

    /**
     * Get military category designation if available
     */
    public String getMilitaryCategoryDesignation() {
        return militaryCategory != null ? militaryCategory.getDesignationFr() : null;
    }

    /**
     * Get seniority level (0-100, higher is more senior)
     */
    public Integer getSeniorityLevel() {
        Integer rankLevel = getRankLevel();
        if (rankLevel == null) return 0;
        
        // Convert rank level to seniority (0-100 scale)
        int baseSeniority = Math.max(0, (rankLevel + 5) * 5);
        return Math.min(100, baseSeniority);
    }

    /**
     * Check if rank has command authority
     */
    public boolean hasCommandAuthority() {
        String category = getRankCategory();
        Integer level = getRankLevel();
        
        return "SENIOR_OFFICER".equals(category) || 
               ("OFFICER".equals(category) && level != null && level >= 5) ||
               ("NCO".equals(category) && level != null && level >= 1);
    }

    /**
     * Get promotion requirements
     */
    public String getPromotionRequirements() {
        String category = getRankCategory();
        
        return switch (category) {
            case "ENLISTED" -> "TRAINING_COMPLETION";
            case "NCO" -> "LEADERSHIP_TRAINING";
            case "OFFICER" -> "COMMAND_EXPERIENCE";
            case "SENIOR_OFFICER" -> "STRATEGIC_LEADERSHIP";
            default -> "EVALUATION_REQUIRED";
        };
    }

    /**
     * Create simplified DTO for dropdowns
     */
    public static MilitaryRankDTO createSimple(Long id, String designationFr, String abbreviationFr, Long militaryCategoryId) {
        return MilitaryRankDTO.builder()
                .id(id)
                .designationFr(designationFr)
                .abbreviationFr(abbreviationFr)
                .militaryCategoryId(militaryCategoryId)
                .build();
    }

    /**
     * Validate required fields are present
     */
    public boolean isValid() {
        return designationFr != null && !designationFr.trim().isEmpty() && 
               abbreviationFr != null && !abbreviationFr.trim().isEmpty() &&
               militaryCategoryId != null;
    }

    /**
     * Get short display for lists (abbreviation - designation)
     */
    public String getShortDisplay() {
        return getDisplayAbbreviation() + " - " + getDisplayText();
    }

    /**
     * Get full display with all languages and category context
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
        
        sb.append(" (").append(abbreviationFr).append(")");
        
        if (militaryCategory != null && militaryCategory.getDesignationFr() != null) {
            sb.append(" - ").append(militaryCategory.getDesignationFr());
        }
        
        return sb.toString();
    }

    /**
     * Get comparison key for sorting (by rank level descending, then designation)
     */
    public String getComparisonKey() {
        Integer level = getRankLevel();
        String levelStr = level != null ? String.format("%03d", 100 - level) : "999";
        return levelStr + "_" + (designationFr != null ? designationFr.toLowerCase() : "zzz");
    }

    /**
     * Get display with rank category
     */
    public String getDisplayWithCategory() {
        return getDisplayText() + " (" + getRankCategory().replace("_", " ").toLowerCase() + ")";
    }

    /**
     * Get display with abbreviation and category
     */
    public String getDisplayWithAbbreviationAndCategory() {
        return getDisplayAbbreviation() + " - " + getDisplayText() + " (" + getRankCategory().replace("_", " ").toLowerCase() + ")";
    }

    /**
     * Get formal military display
     */
    public String getFormalMilitaryDisplay() {
        StringBuilder sb = new StringBuilder();
        
        if (militaryCategory != null && militaryCategory.getDesignationFr() != null) {
            sb.append(militaryCategory.getDesignationFr()).append("-");
        }
        
        sb.append(getDisplayAbbreviation()).append(" (").append(getDisplayText()).append(")");
        
        return sb.toString();
    }

    /**
     * Check if rank is commissioned officer
     */
    public boolean isCommissionedOfficer() {
        String category = getRankCategory();
        return "OFFICER".equals(category) || "SENIOR_OFFICER".equals(category);
    }

    /**
     * Check if rank is non-commissioned officer
     */
    public boolean isNonCommissionedOfficer() {
        return "NCO".equals(getRankCategory());
    }

    /**
     * Check if rank is enlisted personnel
     */
    public boolean isEnlistedPersonnel() {
        return "ENLISTED".equals(getRankCategory());
    }

    /**
     * Get minimum years for promotion
     */
    public Integer getMinimumYearsForPromotion() {
        String category = getRankCategory();
        Integer level = getRankLevel();
        
        return switch (category) {
            case "ENLISTED" -> 2;
            case "NCO" -> level != null && level >= 1 ? 4 : 3;
            case "OFFICER" -> level != null && level >= 6 ? 4 : 3;
            case "SENIOR_OFFICER" -> 5;
            default -> 3;
        };
    }

    /**
     * Get rank insignia description
     */
    public String getRankInsigniaDescription() {
        String category = getRankCategory();
        Integer level = getRankLevel();
        
        if (level == null) return "Standard insignia";
        
        return switch (category) {
            case "SENIOR_OFFICER" -> level >= 10 ? "Stars and eagle" : "Stars";
            case "OFFICER" -> level >= 6 ? "Crown and bars" : "Bars";
            case "NCO" -> level >= 1 ? "Chevrons with crown" : "Chevrons";
            case "ENLISTED" -> level >= -2 ? "Stripes" : "No insignia";
            default -> "Standard insignia";
        };
    }

    /**
     * Get command span description
     */
    public String getCommandSpanDescription() {
        Integer level = getRankLevel();
        if (level == null) return "Individual";
        
        if (level >= 12) return "Army/Service level";
        if (level >= 10) return "Corps/Division level";
        if (level >= 8) return "Brigade/Regiment level";
        if (level >= 6) return "Battalion level";
        if (level >= 4) return "Company level";
        if (level >= 1) return "Platoon/Squad level";
        if (level >= -2) return "Team level";
        return "Individual";
    }

    /**
     * Get typical assignment description
     */
    public String getTypicalAssignment() {
        String category = getRankCategory();
        Integer level = getRankLevel();
        
        if (level == null) return "Various positions";
        
        return switch (category) {
            case "SENIOR_OFFICER" -> level >= 10 ? "High command positions" : "Senior staff positions";
            case "OFFICER" -> level >= 6 ? "Battalion/Company command" : "Platoon leadership";
            case "NCO" -> level >= 1 ? "Squad/Section leadership" : "Team leadership";
            case "ENLISTED" -> level >= -2 ? "Specialist roles" : "Basic military duties";
            default -> "Various positions";
        };
    }

    /**
     * Get retirement eligibility description
     */
    public String getRetirementEligibility() {
        String category = getRankCategory();
        
        return switch (category) {
            case "SENIOR_OFFICER" -> "30 years or age 60";
            case "OFFICER" -> "25 years or age 58";
            case "NCO" -> "20 years or age 55";
            case "ENLISTED" -> "15 years or age 50";
            default -> "Standard military retirement";
        };
    }
}
