package com.calculator.postcode.exception;

import com.calculator.postcode.error.GeographicalError;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GeographicalException extends Exception {

    private String errorCode;
    private String errorMessage;

    public GeographicalException(String message) {
        super(message);
    }

    public GeographicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeographicalException(Throwable cause) {
        super(cause);
    }

    public GeographicalException(GeographicalError error) {
        super(error.getErrorMessage());
        this.errorCode = error.getErrorCode();
        this.errorMessage = error.getErrorMessage();
    }

    public GeographicalException(GeographicalError error, Throwable cause) {
        super(error.getErrorMessage(), cause);
        this.errorCode = error.getErrorCode();
        this.errorMessage = error.getErrorMessage();
    }

}
