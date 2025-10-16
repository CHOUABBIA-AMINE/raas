/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ConsultationDTO
 *	@CreatedOn	: 10-12-2025
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Business / Consultation
 *
 **/

package dz.mdn.raas.business.consultation.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Consultation Data Transfer Object
 * Maps to exact field names from Consultation model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultationDTO {

    private Long id;

    @Size(max = 3, message = "Internal ID must not exceed 3 characters")
    private String internalId;

    @NotBlank(message = "Consultation year is required")
    @Pattern(regexp = "\\d{4}", message = "Consultation year must be 4 digits")
    private String consultationYear;

    @Size(max = 20, message = "Reference must not exceed 20 characters")
    private String reference;

    @Size(max = 300, message = "Arabic designation must not exceed 300 characters")
    private String designationAr;

    @Size(max = 300, message = "English designation must not exceed 300 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 300, message = "French designation must not exceed 300 characters")
    private String designationFr;

    @DecimalMin(value = "0.0", message = "Allocated amount must be positive")
    private Double allocatedAmount;

    @DecimalMin(value = "0.0", message = "Financial estimation must be positive")
    private Double financialEstimation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Size(max = 20, message = "Approval reference must not exceed 20 characters")
    private String approvalReference;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date approvalDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    @Size(max = 500, message = "Observation must not exceed 500 characters")
    private String observation;

    private Integer submissionCount;

    private Double lowestOffer;

    private Double highestOffer;

    private Double averageOffer;

    private Integer daysUntilDeadline;

    private Boolean isExpired;

    private Boolean isActive;
}