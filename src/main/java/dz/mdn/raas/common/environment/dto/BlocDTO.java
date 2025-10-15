/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BlocDTO
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
public class BlocDTO {

    private Long id; // F_00

    @NotBlank(message = "Arabic code is required")
    @Size(max = 20, message = "Arabic code must not exceed 20 characters")
    private String codeAr; // F_01 - required and unique

    @NotBlank(message = "Latin code is required")
    @Size(max = 20, message = "Latin code must not exceed 20 characters")
    private String codeLt; // F_02 - required and unique

    @Size(max = 200, message = "Arabic designation must not exceed 200 characters")
    private String designationAr; // F_03 - optional

    @Size(max = 200, message = "English designation must not exceed 200 characters")
    private String designationEn; // F_04 - optional

    @NotBlank(message = "French designation is required")
    @Size(max = 200, message = "French designation must not exceed 200 characters")
    private String designationFr; // F_05 - required and unique

    public static BlocDTO fromEntity(dz.mdn.raas.common.environment.model.Bloc bloc) {
        if (bloc == null) return null;
        
        return BlocDTO.builder()
                .id(bloc.getId())
                .codeAr(bloc.getCodeAr())
                .codeLt(bloc.getCodeLt())
                .designationAr(bloc.getDesignationAr())
                .designationEn(bloc.getDesignationEn())
                .designationFr(bloc.getDesignationFr())
                .build();
    }

    public dz.mdn.raas.common.environment.model.Bloc toEntity() {
        dz.mdn.raas.common.environment.model.Bloc bloc = new dz.mdn.raas.common.environment.model.Bloc();
        bloc.setId(this.id);
        bloc.setCodeAr(this.codeAr);
        bloc.setCodeLt(this.codeLt);
        bloc.setDesignationAr(this.designationAr);
        bloc.setDesignationEn(this.designationEn);
        bloc.setDesignationFr(this.designationFr);
        return bloc;
    }

    public void updateEntity(dz.mdn.raas.common.environment.model.Bloc bloc) {
        if (this.codeAr != null) {
            bloc.setCodeAr(this.codeAr);
        }
        if (this.codeLt != null) {
            bloc.setCodeLt(this.codeLt);
        }
        if (this.designationAr != null) {
            bloc.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            bloc.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            bloc.setDesignationFr(this.designationFr);
        }
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

    public String getCodeByLanguage(String language) {
        if (language == null) return codeLt;
        
        return switch (language.toLowerCase()) {
            case "ar", "arabic" -> codeAr;
            case "lt", "latin", "en", "english", "fr", "french" -> codeLt;
            default -> codeLt;
        };
    }

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
        return codeLt + " - " + codeAr;
    }

    public String getDisplayTextWithCode() {
        return codeLt + " - " + getDisplayText();
    }

    public String getFullDisplayText() {
        return String.format("%s (%s) - %s", codeLt, codeAr, getDisplayText());
    }

    public boolean isMultilingual() {
        int languageCount = 0;
        if (designationAr != null && !designationAr.trim().isEmpty()) languageCount++;
        if (designationEn != null && !designationEn.trim().isEmpty()) languageCount++;
        if (designationFr != null && !designationFr.trim().isEmpty()) languageCount++;
        return languageCount > 1;
    }

    public String[] getAvailableLanguages() {
        java.util.List<String> languages = new java.util.ArrayList<>();
        
        if (codeAr != null && !codeAr.trim().isEmpty()) {
            languages.add("arabic_code");
        }
        if (codeLt != null && !codeLt.trim().isEmpty()) {
            languages.add("latin_code");
        }
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

    public static BlocDTO createSimple(Long id, String codeLt, String codeAr, String designationFr) {
        return BlocDTO.builder()
                .id(id)
                .codeLt(codeLt)
                .codeAr(codeAr)
                .designationFr(designationFr)
                .build();
    }

    public boolean isValid() {
        return codeAr != null && !codeAr.trim().isEmpty() &&
               codeLt != null && !codeLt.trim().isEmpty() &&
               designationFr != null && !designationFr.trim().isEmpty();
    }

    public boolean hasValidCodeFormat() {
        return codeAr != null && codeAr.matches("^[\\p{InArabic}0-9]{1,20}$") &&
               codeLt != null && codeLt.matches("^[A-Za-z0-9]{1,20}$");
    }

    public String getShortDisplay() {
        return codeLt + " - " + (designationFr != null && designationFr.length() > 30 ? 
                designationFr.substring(0, 30) + "..." : designationFr);
    }
}
