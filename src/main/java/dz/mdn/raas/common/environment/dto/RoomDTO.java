/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RoomDTO
 *	@CreatedOn	: 10-15-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Common / Environment
 *
 **/

package dz.mdn.raas.common.environment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {

    private Long id; // F_00

    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code; // F_01 - required and unique

    @Size(max = 200, message = "Arabic designation must not exceed 200 characters")
    private String designationAr; // F_02 - optional

    @Size(max = 200, message = "English designation must not exceed 200 characters")
    private String designationEn; // F_03 - optional

    @NotBlank(message = "French designation is required")
    @Size(max = 200, message = "French designation must not exceed 200 characters")
    private String designationFr; // F_04 - required and unique

    @NotNull(message = "Bloc ID is required")
    private Long blocId; // F_05 - foreign key to Bloc (required)

    @NotNull(message = "Floor ID is required")
    private Long floorId; // F_06 - foreign key to Floor (required)

    private Long structureId; // F_07 - foreign key to Structure (optional)

    // Additional fields for display purposes (from related entities)
    private String blocCodeLt;
    private String blocCodeAr;
    private String blocDesignationFr;

    private String floorCode;
    private String floorDesignationFr;

    private String structureName;
    private String structureCode;

    private Long shelfCount; // Count of shelfs in this room

    // Combined display information
    private String displayTextWithCode;
    private String fullDisplayText;
    private String locationPath;

    public static RoomDTO fromEntity(dz.mdn.raas.common.environment.model.Room room) {
        if (room == null) return null;
        
        RoomDTO.RoomDTOBuilder builder = RoomDTO.builder()
                .id(room.getId())
                .code(room.getCode())
                .designationAr(room.getDesignationAr())
                .designationEn(room.getDesignationEn())
                .designationFr(room.getDesignationFr());

        // Handle Bloc relationship (required F_05)
        if (room.getBloc() != null) {
            builder.blocId(room.getBloc().getId())
                   .blocCodeLt(room.getBloc().getCodeLt())
                   .blocCodeAr(room.getBloc().getCodeAr())
                   .blocDesignationFr(room.getBloc().getDesignationFr());
        }

        // Handle Floor relationship (required F_06)
        if (room.getFloor() != null) {
            builder.floorId(room.getFloor().getId())
                   .floorCode(room.getFloor().getCode())
                   .floorDesignationFr(room.getFloor().getDesignationFr());
        }

        // Handle Structure relationship (optional F_07)
        if (room.getStructure() != null) {
            builder.structureId(room.getStructure().getId())
                   .structureName(getStructureName(room.getStructure()))
                   .structureCode(getStructureCode(room.getStructure()));
        }

        // Count shelfs (OneToMany relationship)
        if (room.getShelfs() != null) {
            builder.shelfCount((long) room.getShelfs().size());
        } else {
            builder.shelfCount(0L);
        }

        RoomDTO dto = builder.build();
        dto.setDisplayTextWithCode(buildDisplayTextWithCode(dto));
        dto.setFullDisplayText(buildFullDisplayText(dto));
        dto.setLocationPath(buildLocationPath(dto));
        
        return dto;
    }

    public dz.mdn.raas.common.environment.model.Room toEntity() {
        dz.mdn.raas.common.environment.model.Room room = new dz.mdn.raas.common.environment.model.Room();
        room.setId(this.id);
        room.setCode(this.code);
        room.setDesignationAr(this.designationAr);
        room.setDesignationEn(this.designationEn);
        room.setDesignationFr(this.designationFr);
        // Note: relationships must be set by service layer using IDs
        return room;
    }

    public void updateEntity(dz.mdn.raas.common.environment.model.Room room) {
        if (this.code != null) {
            room.setCode(this.code);
        }
        if (this.designationAr != null) {
            room.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            room.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            room.setDesignationFr(this.designationFr);
        }
        // Note: relationship updates must be handled by service layer using IDs
    }

    public String getDefaultDesignation() {
        return designationFr;
    }

    public String getDesignationByLanguage(String language) {
        if (language == null) return designationFr;
        
        return switch (language.toLowerCase()) {
            case "ar", "arabic" -> designationAr != null ? designationAr : designationFr;
            case "en", "english" -> designationEn != null ? designationEn : designationFr;
            case "fr", "french" -> designationFr;
            default -> designationFr;
        };
    }

    public boolean isMultilingual() {
        int languageCount = 0;
        if (designationAr != null && !designationAr.trim().isEmpty()) languageCount++;
        if (designationEn != null && !designationEn.trim().isEmpty()) languageCount++;
        if (designationFr != null && !designationFr.trim().isEmpty()) languageCount++;
        return languageCount > 1;
    }

    public boolean hasStructure() {
        return structureId != null;
    }

    public boolean hasShelfs() {
        return shelfCount != null && shelfCount > 0;
    }

    public boolean isComplete() {
        return blocId != null && floorId != null;
    }

    public String getCapacityStatus() {
        if (shelfCount == null || shelfCount == 0) {
            return "EMPTY";
        } else if (shelfCount < 5) {
            return "LOW";
        } else if (shelfCount < 15) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    public static RoomDTO createSimple(Long id, String code, String designationFr) {
        return RoomDTO.builder()
                .id(id)
                .code(code)
                .designationFr(designationFr)
                .displayTextWithCode(code + " - " + designationFr)
                .build();
    }

    // ========== HELPER METHODS ==========

    private static String getStructureName(dz.mdn.raas.common.administration.model.Structure structure) {
        // Assuming structure has name field - adjust based on actual Structure model
        return "Structure #" + structure.getId();
    }

    private static String getStructureCode(dz.mdn.raas.common.administration.model.Structure structure) {
        // Assuming structure has code field - adjust based on actual Structure model
        return "STR-" + structure.getId();
    }

    private static String buildDisplayTextWithCode(RoomDTO dto) {
        return dto.getCode() + " - " + dto.getDesignationFr();
    }

    private static String buildFullDisplayText(RoomDTO dto) {
        StringBuilder sb = new StringBuilder();
        sb.append(dto.getCode()).append(" - ").append(dto.getDesignationFr());
        
        if (dto.getFloorDesignationFr() != null) {
            sb.append(" (Floor: ").append(dto.getFloorDesignationFr()).append(")");
        }
        
        if (dto.getBlocDesignationFr() != null) {
            sb.append(" - Bloc: ").append(dto.getBlocDesignationFr());
        }
        
        if (dto.getStructureName() != null) {
            sb.append(" [").append(dto.getStructureName()).append("]");
        }
        
        return sb.toString();
    }

    private static String buildLocationPath(RoomDTO dto) {
        StringBuilder sb = new StringBuilder();
        
        if (dto.getBlocCodeLt() != null) {
            sb.append(dto.getBlocCodeLt());
        }
        
        if (dto.getFloorCode() != null) {
            if (sb.length() > 0) sb.append("/");
            sb.append(dto.getFloorCode());
        }
        
        if (dto.getCode() != null) {
            if (sb.length() > 0) sb.append("/");
            sb.append(dto.getCode());
        }
        
        return sb.toString();
    }
    
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
}
