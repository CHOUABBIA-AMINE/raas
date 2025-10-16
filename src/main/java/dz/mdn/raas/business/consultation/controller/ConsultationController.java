/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ConsultationController
 *	@CreatedOn	: 10-12-2025
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Business / Consultation
 *
 **/

package dz.mdn.raas.business.consultation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.mdn.raas.business.consultation.dto.ConsultationDTO;
import dz.mdn.raas.business.consultation.service.ConsultationService;
import dz.mdn.raas.business.consultation.service.ConsultationService.ConsultationStatistics;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Enhanced Consultation REST Controller
 * Provides comprehensive consultation management endpoints
 */
@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
@Slf4j
public class ConsultationController {

    private final ConsultationService consultationService;

    /**
     * Create new consultation
     */
    @PostMapping
    public ResponseEntity<ConsultationDTO> createConsultation(@Valid @RequestBody ConsultationDTO consultationDTO) {

        log.info("Creating consultation for year: {}", consultationDTO.getConsultationYear());

        ConsultationDTO createdConsultation = consultationService.createConsultation(consultationDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdConsultation);
    }

    /**
     * Get consultation by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDTO> getConsultationById(@PathVariable Long id) {

        log.debug("Fetching consultation with ID: {}", id);

        ConsultationDTO consultation = consultationService.getConsultationById(id);

        return ResponseEntity.ok(consultation);
    }

    /**
     * Get consultation statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ConsultationStatistics> getConsultationStatistics(
            @RequestParam 
            @Pattern(regexp = "\\d{4}", message = "Year must be 4 digits")
            //@Parameter(description = "Year for statistics", example = "2025") 
            String year) {

        log.debug("Fetching consultation statistics for year: {}", year);

        ConsultationStatistics statistics = consultationService.getConsultationStatistics(year);

        return ResponseEntity.ok(statistics);
    }
}