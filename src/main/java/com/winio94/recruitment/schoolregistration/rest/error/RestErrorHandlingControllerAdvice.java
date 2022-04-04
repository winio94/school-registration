package com.winio94.recruitment.schoolregistration.rest.error;

import com.winio94.recruitment.schoolregistration.service.Errors;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
class RestErrorHandlingControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Errors handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult()
          .getAllErrors()
          .forEach((error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.add(fieldName + " " + errorMessage);
          });
        return new Errors(errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    Errors handleValidationExceptions(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations()
          .forEach((error) -> {
              String fieldName = StringUtils.substringAfterLast(error.getPropertyPath()
                                                                     .toString(), ".");
              String errorMessage = error.getMessage();
              errors.add(fieldName + " " + errorMessage);
          });
        return new Errors(errors);
    }
}