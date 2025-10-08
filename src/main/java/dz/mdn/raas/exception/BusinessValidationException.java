package dz.mdn.raas.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when business validation rules are violated
 */
public class BusinessValidationException extends RaasException {

    private static final long serialVersionUID = -7975433501929245642L;

	public BusinessValidationException(String message) {
        super(message, "BUSINESS_VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    public BusinessValidationException(String message, String field, Object value) {
        super(
            String.format("Validation failed for field '%s' with value '%s': %s", field, value, message),
            "BUSINESS_VALIDATION_ERROR",
            HttpStatus.BAD_REQUEST,
            field, value
        );
    }
}