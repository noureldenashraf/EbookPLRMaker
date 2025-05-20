package nour.ebookplrmaker.exception;


import nour.ebookplrmaker.model.ExceptionDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions (Exception e) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDetails
                (
                 e.getMessage()
                ,e.getClass().getSimpleName()
                ));

}
}
