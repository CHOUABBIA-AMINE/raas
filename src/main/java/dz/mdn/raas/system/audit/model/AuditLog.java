/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: AuditLog
 *	@CreatedOn	: 10-27-2025
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Audit
 *
 **/

package dz.mdn.raas.system.audit.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuditLog Entity for tracking all business operations
 * Maps to table T_SYS_AUDIT with comprehensive audit information
 */
@Entity(name = "AuditLog")
@Table(name = "T_SYS_AUDIT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Column(name = "F_01", nullable = false, length = 100)
    private String entityName; // e.g., "Submission", "Consultation"

    @Column(name = "F_02", nullable = false)
    private Long entityId; // ID of the affected entity

    @Enumerated(EnumType.STRING)
    @Column(name = "F_03", nullable = false, length = 20)
    private AuditAction action; // CREATE, UPDATE, DELETE, READ

    @Column(name = "F_04", nullable = true, length = 100)
    private String username; // User who performed the action

    @Column(name = "F_05", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "F_06", nullable = true, length = 45)
    private String ipAddress;

    @Column(name = "F_07", nullable = true, length = 500)
    private String userAgent;

    @Column(name = "F_08", nullable = true, length = 200)
    private String methodName; // Service method that was called

    @Column(name = "F_09", nullable = true, columnDefinition = "TEXT")
    private String oldValues; // JSON of old values (for updates)

    @Column(name = "F_10", nullable = true, columnDefinition = "TEXT")
    private String newValues; // JSON of new values (for creates/updates)

    @Column(name = "F_11", nullable = true, columnDefinition = "TEXT")
    private String parameters; // JSON of method parameters

    @Column(name = "F_12", nullable = true, length = 1000)
    private String description; // Human-readable description

    @Enumerated(EnumType.STRING)
    @Column(name = "F_13", nullable = false, length = 20)
    private AuditStatus status; // SUCCESS, FAILED, PARTIAL

    @Column(name = "F_14", nullable = true, columnDefinition = "TEXT")
    private String errorMessage; // Error details if status is FAILED

    @Column(name = "F_15", nullable = true)
    private Long duration; // Operation duration in milliseconds

    @Column(name = "F_16", nullable = true, length = 100)
    private String sessionId; // User session identifier

    @Column(name = "F_17", nullable = true, length = 50)
    private String module; // e.g., "CONSULTATION", "CONTRACT", "PROVIDER"

    @Column(name = "F_18", nullable = true, length = 50)
    private String businessProcess; // e.g., "SUBMISSION_CREATION", "CONTRACT_APPROVAL"

    @Column(name = "F_19", nullable = true)
    private Long parentAuditId; // For linking related audit entries

    @Column(name = "F_20", nullable = true, columnDefinition = "TEXT")
    private String metadata; // Additional JSON metadata

    /**
     * Audit Action Types
     */
    public enum AuditAction {
        CREATE, UPDATE, DELETE, READ, APPROVE, REJECT, SUBMIT, CANCEL, ARCHIVE, RESTORE
    }

    /**
     * Audit Status Types
     */
    public enum AuditStatus {
        SUCCESS, FAILED, PARTIAL
    }
}
