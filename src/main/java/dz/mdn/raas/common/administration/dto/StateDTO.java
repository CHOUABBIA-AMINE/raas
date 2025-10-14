/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StateDTO
 *	@CreatedOn	: 10-14-2025
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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateDTO {

    private Long id; // F_00

    @NotBlank(message = "Code is required")
    @Size(max = 100, message = "Code must not exceed 100 characters")
    private String code; // F_01 - required and unique

    @NotBlank(message = "Arabic designation is required")
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr; // F_02 - required and unique

    @NotBlank(message = "Latin designation is required")
    @Size(max = 100, message = "Latin designation must not exceed 100 characters")
    private String designationLt; // F_03 - required and unique

    public static StateDTO fromEntity(dz.mdn.raas.common.administration.model.State state) {
        if (state == null) return null;
        
        return StateDTO.builder()
                .id(state.getId())
                .code(state.getCode())
                .designationAr(state.getDesignationAr())
                .designationLt(state.getDesignationLt())
                .build();
    }

    public dz.mdn.raas.common.administration.model.State toEntity() {
        dz.mdn.raas.common.administration.model.State state = new dz.mdn.raas.common.administration.model.State();
        state.setId(this.id);
        state.setCode(this.code);
        state.setDesignationAr(this.designationAr);
        state.setDesignationLt(this.designationLt);
        return state;
    }

    public void updateEntity(dz.mdn.raas.common.administration.model.State state) {
        if (this.code != null) {
            state.setCode(this.code);
        }
        if (this.designationAr != null) {
            state.setDesignationAr(this.designationAr);
        }
        if (this.designationLt != null) {
            state.setDesignationLt(this.designationLt);
        }
    }

    public String getDefaultDesignation() {
        return designationLt;
    }

    public String getDesignationByLanguage(String language) {
        if (language == null) return designationLt;
        
        return switch (language.toLowerCase()) {
            case "ar", "arabic" -> designationAr;
            case "lt", "latin", "en", "english" -> designationLt;
            default -> designationLt;
        };
    }

    public String getDisplayText() {
        return String.format("%s - %s", code, designationLt);
    }

    public String getDisplayTextAr() {
        return String.format("%s - %s", code, designationAr);
    }

    public boolean isComplete() {
        return code != null && !code.trim().isEmpty() &&
               designationAr != null && !designationAr.trim().isEmpty() &&
               designationLt != null && !designationLt.trim().isEmpty();
    }
}
