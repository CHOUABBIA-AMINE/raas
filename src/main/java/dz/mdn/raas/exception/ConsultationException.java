package dz.mdn.raas.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for consultation-specific errors
 */
public class ConsultationException extends RaasException {

    private static final long serialVersionUID = 5512195457454064972L;

	public ConsultationException(String message) {
        super(message, "CONSULTATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    public ConsultationException(String message, HttpStatus status) {
        super(message, "CONSULTATION_ERROR", status);
    }
}