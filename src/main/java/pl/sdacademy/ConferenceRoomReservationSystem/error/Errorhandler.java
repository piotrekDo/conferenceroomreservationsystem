package pl.sdacademy.ConferenceRoomReservationSystem.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class Errorhandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorEntity<Map<String, List<String>>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            List<String> params = errors.getOrDefault(fieldName, new ArrayList<>());
            params.add(errorMessage);
            errors.put(fieldName, params);
        });
        return new ResponseEntity<>(new ErrorEntity<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    ResponseEntity<ErrorEntity<String>> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorEntity<>(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<ErrorEntity<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorEntity<>(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
