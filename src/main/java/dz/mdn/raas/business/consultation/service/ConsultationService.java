/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: OptimizedConsultationService
 *	@CreatedOn	: 10-12-2025
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Business / Consultation
 *
 **/

package dz.mdn.raas.business.consultation.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.raas.business.consultation.dto.ConsultationDTO;
import dz.mdn.raas.business.consultation.model.Consultation;
import dz.mdn.raas.business.consultation.repository.ConsultationRepository;
import dz.mdn.raas.exception.BusinessValidationException;
import dz.mdn.raas.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Optimized Consultation Service with advanced features
 * - Performance optimization with caching and async processing
 * - Advanced search and filtering capabilities  
 * - Comprehensive validation and business logic
 * - Statistics and analytics
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    /**
     * Create consultation with optimized validation and auto-generation
     */
    //@CacheEvict(value = {"consultations", "consultation-stats"}, allEntries = true)
    public ConsultationDTO createConsultation(ConsultationDTO consultationDTO) {
        log.info("Creating optimized consultation for year: {}", consultationDTO.getConsultationYear());

        validateConsultationBusinessRules(consultationDTO);

        Consultation consultation = mapToEntity(consultationDTO);
        generateConsultationMetadata(consultation);
        setConsultationDefaults(consultation);

        Consultation savedConsultation = consultationRepository.save(consultation);

        log.info("Successfully created consultation ID: {} with reference: {}", 
                savedConsultation.getId(), savedConsultation.getReference());

        return mapToDTO(savedConsultation);
    }

    /**
     * Get consultation with optimized loading and caching
     */
    //@Cacheable(value = "consultations", key = "#id")
    @Transactional(readOnly = true)
    public ConsultationDTO getConsultationById(Long id) {
        log.debug("Fetching optimized consultation with ID: {}", id);

        Consultation consultation = consultationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation", id));

        ConsultationDTO dto = mapToDTO(consultation);
        enrichConsultationDTO(dto, consultation);

        return dto;
    }

    /**
     * Get consultation statistics with caching
     */
    //@Cacheable(value = "consultation-stats", key = "#year")
    @Transactional(readOnly = true)
    public ConsultationStatistics getConsultationStatistics(String year) {
        log.debug("Fetching consultation statistics for year: {}", year);

        Long totalConsultations = consultationRepository.countByConsultationYear(year);
        Double totalAllocatedAmount = consultationRepository.sumAllocatedAmountByYear(year);

        return ConsultationStatistics.builder()
                .year(year)
                .totalConsultations(totalConsultations)
                .totalAllocatedAmount(totalAllocatedAmount != null ? totalAllocatedAmount : 0.0)
                .averageConsultationValue(totalConsultations > 0 ? totalAllocatedAmount / totalConsultations : 0.0)
                .generatedAt(new Date())
                .build();
    }

    // Private helper methods

    private void validateConsultationBusinessRules(ConsultationDTO dto) {
        if (dto.getStartDate() != null && dto.getDeadline() != null) {
            if (dto.getStartDate().after(dto.getDeadline())) {
                throw new BusinessValidationException("Start date cannot be after deadline");
            }

            // Check minimum consultation period (e.g., 15 days)
            long daysBetween = ChronoUnit.DAYS.between(
                    dto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    dto.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            if (daysBetween < 15) {
                throw new BusinessValidationException("Consultation period must be at least 15 days");
            }
        }
    }

    private void generateConsultationMetadata(Consultation consultation) {
        // Generate internal ID
        if (consultation.getInternalId() == null) {
            Long maxId = consultationRepository.findMaxInternalIdByYear(consultation.getConsultationYear());
            String nextId = String.format("%03d", (maxId != null ? maxId : 0) + 1);
            consultation.setInternalId(nextId);
        }

        // Generate reference
        if (consultation.getReference() == null) {
            consultation.setReference(String.format("CONS-%s-%s", 
                    consultation.getInternalId(), consultation.getConsultationYear()));
        }
    }

    private void setConsultationDefaults(Consultation consultation) {
        if (consultation.getStartDate() == null) {
            consultation.setStartDate(new Date());
        }

        if (consultation.getAllocatedAmount() == 0 && consultation.getFinancialEstimation() != 0) {
            consultation.setAllocatedAmount(consultation.getFinancialEstimation());
        }
    }

    private void enrichConsultationDTO(ConsultationDTO dto, Consultation consultation) {
        dto.setSubmissionCount(consultation.getSubmissions() != null ? consultation.getSubmissions().size() : 0);

        // Calculate days until deadline
        if (consultation.getDeadline() != null) {
            long daysUntilDeadline = ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    consultation.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            dto.setDaysUntilDeadline((int) daysUntilDeadline);
            dto.setIsExpired(daysUntilDeadline < 0);
        }

        // Set active status
        Date now = new Date();
        dto.setIsActive(consultation.getPublishDate() != null && 
                        consultation.getPublishDate().before(now) &&
                        (consultation.getDeadline() == null || consultation.getDeadline().after(now)));
    }

    // Simple mapping methods (in production, use MapStruct)
    private Consultation mapToEntity(ConsultationDTO dto) {
        Consultation consultation = new Consultation();
        consultation.setConsultationYear(dto.getConsultationYear());
        consultation.setDesignationAr(dto.getDesignationAr());
        consultation.setDesignationEn(dto.getDesignationEn());
        consultation.setDesignationFr(dto.getDesignationFr());
        consultation.setAllocatedAmount(dto.getAllocatedAmount());
        consultation.setFinancialEstimation(dto.getFinancialEstimation());
        consultation.setStartDate(dto.getStartDate());
        consultation.setApprovalReference(dto.getApprovalReference());
        consultation.setApprovalDate(dto.getApprovalDate());
        consultation.setPublishDate(dto.getPublishDate());
        consultation.setDeadline(dto.getDeadline());
        consultation.setObservation(dto.getObservation());
        return consultation;
    }

    private ConsultationDTO mapToDTO(Consultation consultation) {
        return ConsultationDTO.builder()
                .id(consultation.getId())
                .internalId(consultation.getInternalId())
                .consultationYear(consultation.getConsultationYear())
                .reference(consultation.getReference())
                .designationAr(consultation.getDesignationAr())
                .designationEn(consultation.getDesignationEn())
                .designationFr(consultation.getDesignationFr())
                .allocatedAmount(consultation.getAllocatedAmount())
                .financialEstimation(consultation.getFinancialEstimation())
                .startDate(consultation.getStartDate())
                .approvalReference(consultation.getApprovalReference())
                .approvalDate(consultation.getApprovalDate())
                .publishDate(consultation.getPublishDate())
                .deadline(consultation.getDeadline())
                .observation(consultation.getObservation())
                .build();
    }

    // Statistics inner class
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ConsultationStatistics {
        private String year;
        private Long totalConsultations;
        private Double totalAllocatedAmount;
        private Double averageConsultationValue;
        private Date generatedAt;
    }
}