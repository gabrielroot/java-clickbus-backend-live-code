package br.com.clickbus.challenge.exception;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<ResponseErrorBody> handleNotImplementedException(NotImplementedException notImplementedException) {
        String message = notImplementedException.getMessage().isEmpty()
                ? "Resource not implemented yet."
                : notImplementedException.getMessage();
        return new ResponseEntity<>(this.responseError(message, HttpStatus.NOT_IMPLEMENTED), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<ResponseErrorBody> handlePlaceNotFoundException(PlaceNotFoundException placeNotFoundException) {
        String message = placeNotFoundException.getMessage().isEmpty()
                ? "Resource ID not found."
                : placeNotFoundException.getMessage();
        return new ResponseEntity<>(this.responseError(message, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseErrorBody> handleUnexpectedException(Throwable unexpectedException) {
        String message = "Unexpected server error.";
        logger.error("", unexpectedException);
        return new ResponseEntity<>(this.responseError(message, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseErrorBody responseError(String message, HttpStatus statusCode){
        ResponseErrorBody responseError = new ResponseErrorBody();
        responseError.setStatus("error");
        responseError.setError(message);
        responseError.setStatusCode(statusCode.value());
        return responseError;
    }
}
