/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StructureTypeDTO
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
 * StructureType Data Transfer Object
 * Maps exactly to StructureType model fields: F_00=id, F_01=designationAr, F_02=designationEn, F_03=designationFr, F_04=acronymAr, F_05=acronymEn, F_06=acronymFr
 * F_03 (designationFr) has unique constraint and is required
 * F_06 (acronymFr) has unique constraint and is required
 * F_01 (designationAr), F_02 (designationEn), F_04 (acronymAr), F_05 (acronymEn) are optional
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StructureTypeDTO {

    private Long id; // F_00

    @Size(max = 200, message = "Arabic designation must not exceed 200 characters")
    private String designationAr; // F_01 - optional

    @Size(max = 200, message = "English designation must not exceed 200 characters")
    private String designationEn; // F_02 - optional

    @NotBlank(message = "French designation is required")
    @Size(max = 200, message = "French designation must not exceed 200 characters")
    private String designationFr; // F_03 - required and unique

    @Size(max = 20, message = "Arabic acronym must not exceed 20 characters")
    private String acronymAr; // F_04 - optional

    @Size(max = 20, message = "English acronym must not exceed 20 characters")
    private String acronymEn; // F_05 - optional

    @NotBlank(message = "French acronym is required")
    @Size(max = 20, message = "French acronym must not exceed 20 characters")
    private String acronymFr; // F_06 - required and unique

    /**
     * Create DTO from entity
     */
    public static StructureTypeDTO fromEntity(dz.mdn.raas.common.administration.model.StructureType structureType) {
        if (structureType == null) return null;
        
        return StructureTypeDTO.builder()
                .id(structureType.getId())
                .designationAr(structureType.getDesignationAr())
                .designationEn(structureType.getDesignationEn())
                .designationFr(structureType.getDesignationFr())
                .acronymAr(structureType.getAcronymAr())
                .acronymEn(structureType.getAcronymEn())
                .acronymFr(structureType.getAcronymFr())
                .build();
    }

    /**
     * Convert to entity
     */
    public dz.mdn.raas.common.administration.model.StructureType toEntity() {
        dz.mdn.raas.common.administration.model.StructureType structureType = 
            new dz.mdn.raas.common.administration.model.StructureType();
        structureType.setId(this.id);
        structureType.setDesignationAr(this.designationAr);
        structureType.setDesignationEn(this.designationEn);
        structureType.setDesignationFr(this.designationFr);
        structureType.setAcronymAr(this.acronymAr);
        structureType.setAcronymEn(this.acronymEn);
        structureType.setAcronymFr(this.acronymFr);
        return structureType;
    }

    /**
     * Update entity from DTO
     */
    public void updateEntity(dz.mdn.raas.common.administration.model.StructureType structureType) {
        if (this.designationAr != null) {
            structureType.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            structureType.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            structureType.setDesignationFr(this.designationFr);
        }
        if (this.acronymAr != null) {
            structureType.setAcronymAr(this.acronymAr);
        }
        if (this.acronymEn != null) {
            structureType.setAcronymEn(this.acronymEn);
        }
        if (this.acronymFr != null) {
            structureType.setAcronymFr(this.acronymFr);
        }
    }

    /**
     * Get default designation (French as it's required)
     */
    public String getDefaultDesignation() {
        return designationFr;
    }

    /**
     * Get default acronym (French as it's required)
     */
    public String getDefaultAcronym() {
        return acronymFr;
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
     * Get acronym by language preference
     */
    public String getAcronymByLanguage(String language) {
        if (language == null) return acronymFr;
        
        return switch (language.toLowerCase()) {
            case "ar", "arabic" -> acronymAr != null ? acronymAr : acronymFr;
            case "en", "english" -> acronymEn != null ? acronymEn : acronymFr;
            case "fr", "french" -> acronymFr;
            default -> acronymFr;
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
     * Get display acronym with priority: French acronym > English acronym > Arabic acronym
     */
    public String getDisplayAcronym() {
        if (acronymFr != null && !acronymFr.trim().isEmpty()) {
            return acronymFr;
        }
        if (acronymEn != null && !acronymEn.trim().isEmpty()) {
            return acronymEn;
        }
        if (acronymAr != null && !acronymAr.trim().isEmpty()) {
            return acronymAr;
        }
        return "N/A";
    }

    /**
     * Check if structure type has multiple language support
     */
    public boolean isMultilingual() {
        int languageCount = 0;
        if (designationAr != null && !designationAr.trim().isEmpty()) languageCount++;
        if (designationEn != null && !designationEn.trim().isEmpty()) languageCount++;
        if (designationFr != null && !designationFr.trim().isEmpty()) languageCount++;
        return languageCount > 1;
    }

    /**
     * Get available languages for this structure type
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
     * Get structure type category based on French designation analysis
     */
    public String getStructureCategory() {
        if (designationFr == null) return "UNKNOWN";
        
        String designation = designationFr.toLowerCase();
        
        // Command structures
        if (designation.contains("commandement") || designation.contains("état-major") || 
            designation.contains("quartier général") || designation.contains("hq")) {
            return "COMMAND";
        }
        
        // Administrative structures
        if (designation.contains("direction") || designation.contains("bureau") || 
            designation.contains("service") || designation.contains("département")) {
            return "ADMINISTRATIVE";
        }
        
        // Operational structures
        if (designation.contains("brigade") || designation.contains("régiment") || 
            designation.contains("bataillon") || designation.contains("escadron")) {
            return "OPERATIONAL";
        }
        
        // Support structures
        if (designation.contains("soutien") || designation.contains("appui") || 
            designation.contains("logistique") || designation.contains("maintenance")) {
            return "SUPPORT";
        }
        
        // Training structures
        if (designation.contains("école") || designation.contains("centre de formation") || 
            designation.contains("académie") || designation.contains("institut")) {
            return "TRAINING";
        }
        
        // Medical structures
        if (designation.contains("hôpital") || designation.contains("infirmerie") || 
            designation.contains("santé") || designation.contains("médical")) {
            return "MEDICAL";
        }
        
        // Technical structures
        if (designation.contains("technique") || designation.contains("ingénierie") || 
            designation.contains("maintenance") || designation.contains("réparation")) {
            return "TECHNICAL";
        }
        
        // Intelligence structures
        if (designation.contains("renseignement") || designation.contains("intelligence") || 
            designation.contains("sécurité") || designation.contains("surveillance")) {
            return "INTELLIGENCE";
        }
        
        // Communications structures
        if (designation.contains("communication") || designation.contains("transmissions") || 
            designation.contains("télécommunications") || designation.contains("signal")) {
            return "COMMUNICATIONS";
        }
        
        // Logistics structures
        if (designation.contains("logistique") || designation.contains("approvisionnement") || 
            designation.contains("transport") || designation.contains("distribution")) {
            return "LOGISTICS";
        }
        
        return "GENERAL";
    }

    /**
     * Get organizational level based on designation analysis
     */
    public String getOrganizationalLevel() {
        if (designationFr == null) return "UNKNOWN";
        
        String designation = designationFr.toLowerCase();
        
        // Strategic level
        if (designation.contains("état-major") || designation.contains("commandement supérieur") || 
            designation.contains("direction générale") || designation.contains("hq")) {
            return "STRATEGIC";
        }
        
        // Operational level
        if (designation.contains("brigade") || designation.contains("division") || 
            designation.contains("région") || designation.contains("zone")) {
            return "OPERATIONAL";
        }
        
        // Tactical level
        if (designation.contains("régiment") || designation.contains("bataillon") || 
            designation.contains("escadron") || designation.contains("compagnie")) {
            return "TACTICAL";
        }
        
        // Unit level
        if (designation.contains("section") || designation.contains("peloton") || 
            designation.contains("équipe") || designation.contains("groupe")) {
            return "UNIT";
        }
        
        // Administrative level
        if (designation.contains("bureau") || designation.contains("service") || 
            designation.contains("département") || designation.contains("cellule")) {
            return "ADMINISTRATIVE";
        }
        
        return "INTERMEDIATE";
    }

    /**
     * Get structure size category
     */
    public String getStructureSize() {
        return switch (getOrganizationalLevel()) {
            case "STRATEGIC" -> "LARGE";
            case "OPERATIONAL" -> "LARGE";
            case "TACTICAL" -> "MEDIUM";
            case "UNIT" -> "SMALL";
            case "ADMINISTRATIVE" -> "MEDIUM";
            default -> "MEDIUM";
        };
    }

    /**
     * Get command authority level
     */
    public String getCommandAuthority() {
        return switch (getStructureCategory()) {
            case "COMMAND" -> "HIGH";
            case "OPERATIONAL" -> "HIGH";
            case "ADMINISTRATIVE" -> "MEDIUM";
            case "SUPPORT", "LOGISTICS" -> "MEDIUM";
            case "TRAINING", "MEDICAL" -> "MEDIUM";
            case "TECHNICAL", "COMMUNICATIONS" -> "LOW";
            case "INTELLIGENCE" -> "SPECIAL";
            default -> "MEDIUM";
        };
    }

    /**
     * Get structure priority for organizational purposes
     */
    public int getStructurePriority() {
        return switch (getStructureCategory()) {
            case "COMMAND" -> 1;
            case "OPERATIONAL" -> 2;
            case "INTELLIGENCE" -> 3;
            case "ADMINISTRATIVE" -> 4;
            case "SUPPORT" -> 5;
            case "LOGISTICS" -> 6;
            case "COMMUNICATIONS" -> 7;
            case "TECHNICAL" -> 8;
            case "MEDICAL" -> 9;
            case "TRAINING" -> 10;
            default -> 11;
        };
    }

    /**
     * Check if structure requires security clearance
     */
    public boolean requiresSecurityClearance() {
        String category = getStructureCategory();
        return "COMMAND".equals(category) || "INTELLIGENCE".equals(category) || 
               "OPERATIONAL".equals(category);
    }

    /**
     * Get typical personnel count range
     */
    public String getTypicalPersonnelRange() {
        return switch (getOrganizationalLevel()) {
            case "STRATEGIC" -> "1000+";
            case "OPERATIONAL" -> "500-2000";
            case "TACTICAL" -> "100-500";
            case "UNIT" -> "10-100";
            case "ADMINISTRATIVE" -> "20-200";
            default -> "50-300";
        };
    }

    /**
     * Get structure mobility
     */
    public String getStructureMobility() {
        return switch (getStructureCategory()) {
            case "OPERATIONAL" -> "MOBILE";
            case "SUPPORT", "LOGISTICS" -> "SEMI_MOBILE";
            case "COMMAND", "ADMINISTRATIVE" -> "STATIC";
            case "TRAINING", "MEDICAL" -> "STATIC";
            case "TECHNICAL", "COMMUNICATIONS" -> "SEMI_MOBILE";
            case "INTELLIGENCE" -> "FLEXIBLE";
            default -> "SEMI_MOBILE";
        };
    }

    /**
     * Create simplified DTO for dropdowns
     */
    public static StructureTypeDTO createSimple(Long id, String designationFr, String acronymFr) {
        return StructureTypeDTO.builder()
                .id(id)
                .designationFr(designationFr)
                .acronymFr(acronymFr)
                .build();
    }

    /**
     * Validate required fields are present
     */
    public boolean isValid() {
        return designationFr != null && !designationFr.trim().isEmpty() && 
               acronymFr != null && !acronymFr.trim().isEmpty();
    }

    /**
     * Get short display for lists
     */
    public String getShortDisplay() {
        return acronymFr + " - " + (designationFr != null && designationFr.length() > 30 ? 
                designationFr.substring(0, 30) + "..." : designationFr);
    }

    /**
     * Get full display with all languages
     */
    public String getFullDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append(acronymFr).append(" - ").append(designationFr);
        
        if (designationEn != null && !designationEn.equals(designationFr)) {
            sb.append(" / ").append(acronymEn != null ? acronymEn : "").append(" - ").append(designationEn);
        }
        
        if (designationAr != null) {
            sb.append(" / ").append(acronymAr != null ? acronymAr : "").append(" - ").append(designationAr);
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
     * Get display with structure category
     */
    public String getDisplayWithCategory() {
        return acronymFr + " - " + designationFr + " (" + getStructureCategory().replace("_", " ").toLowerCase() + ")";
    }

    /**
     * Get display with acronym and designation
     */
    public String getDisplayWithAcronym() {
        return acronymFr + " - " + designationFr;
    }

    /**
     * Check if structure has operational role
     */
    public boolean hasOperationalRole() {
        String category = getStructureCategory();
        return "OPERATIONAL".equals(category) || "COMMAND".equals(category) || 
               "SUPPORT".equals(category) || "LOGISTICS".equals(category);
    }

    /**
     * Get structure classification level
     */
    public String getClassificationLevel() {
        return switch (getStructureCategory()) {
            case "INTELLIGENCE", "COMMAND" -> "CLASSIFIED";
            case "OPERATIONAL" -> "RESTRICTED";
            case "ADMINISTRATIVE", "SUPPORT" -> "INTERNAL";
            case "TRAINING", "MEDICAL" -> "PUBLIC";
            default -> "INTERNAL";
        };
    }

    /**
     * Get reporting frequency
     */
    public String getReportingFrequency() {
        return switch (getOrganizationalLevel()) {
            case "STRATEGIC" -> "WEEKLY";
            case "OPERATIONAL" -> "DAILY";
            case "TACTICAL" -> "DAILY";
            case "UNIT" -> "SHIFT";
            case "ADMINISTRATIVE" -> "WEEKLY";
            default -> "DAILY";
        };
    }

    /**
     * Check if structure is deployable
     */
    public boolean isDeployable() {
        String mobility = getStructureMobility();
        return "MOBILE".equals(mobility) || "SEMI_MOBILE".equals(mobility) || "FLEXIBLE".equals(mobility);
    }

    /**
     * Get structure establishment requirements
     */
    public String[] getEstablishmentRequirements() {
        return switch (getStructureCategory()) {
            case "COMMAND" -> new String[]{"SECURE_FACILITY", "COMMUNICATIONS", "STAFF_QUARTERS"};
            case "OPERATIONAL" -> new String[]{"BARRACKS", "EQUIPMENT_STORAGE", "TRAINING_AREA"};
            case "ADMINISTRATIVE" -> new String[]{"OFFICE_SPACE", "ARCHIVE_STORAGE", "MEETING_ROOMS"};
            case "SUPPORT" -> new String[]{"WORKSHOPS", "STORAGE_FACILITY", "EQUIPMENT_YARD"};
            case "LOGISTICS" -> new String[]{"WAREHOUSES", "LOADING_DOCKS", "TRANSPORT_POOL"};
            case "MEDICAL" -> new String[]{"MEDICAL_FACILITY", "PATIENT_WARDS", "EMERGENCY_ROOM"};
            case "TRAINING" -> new String[]{"CLASSROOMS", "TRAINING_GROUNDS", "DORMITORIES"};
            case "TECHNICAL" -> new String[]{"LABORATORIES", "WORKSHOPS", "TESTING_FACILITY"};
            case "COMMUNICATIONS" -> new String[]{"COMMUNICATIONS_CENTER", "ANTENNA_FARM", "SERVER_ROOM"};
            case "INTELLIGENCE" -> new String[]{"SECURE_FACILITY", "ANALYSIS_CENTER", "BRIEFING_ROOM"};
            default -> new String[]{"BASIC_FACILITY", "OFFICE_SPACE"};
        };
    }
}
