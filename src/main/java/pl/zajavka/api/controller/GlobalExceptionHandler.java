package pl.zajavka.api.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.webjars.NotFoundException;

import java.util.Optional;

//wzorzec projektowy Visitor
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


//    jakiego rodzaju wyjątek ma obsłużyć
    @ExceptionHandler(Exception.class)
    public ModelAndView handlerException (Exception ex){
String message = "Unexpected exception occurred: [%s]".formatted(ex.getMessage());
log.error(message,ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage",message);
        return modelAndView;

    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handlerException (NotFoundException ex){
        String message = "Could not found a resource: [%s]".formatted(ex.getMessage());
        log.error(message,ex);
    ModelAndView modelAndView = new ModelAndView("error");
    modelAndView.addObject("errorMessage",message);
    return modelAndView;
    }


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handlerException (EntityNotFoundException ex){
        String message = "Could not found a resource: [%s]".formatted(ex.getMessage());
        log.error(message,ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage",message);
        return modelAndView;

    }

//    @ExceptionHandler(BindException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ModelAndView handlerException ( BindException ex) {
//        String message = (" Bad request for field: [%s] wrong value: [%s]").formatted(
//                Optional.ofNullable(ex.getFieldError()).map(FieldError::getField).orElse(null),
//                Optional.ofNullable(ex.getFieldError()).map(FieldError::getRejectedValue).orElse(null)
//
//        );
//        log.error(message, ex);
//        ModelAndView modelAndView = new ModelAndView("error");
//        modelAndView.addObject("errorMessage", message);
//        return modelAndView;
//
//    }
@ExceptionHandler(BindException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ModelAndView handleBindException(BindException ex) {
    String field = Optional.ofNullable(ex.getFieldError()).map(FieldError::getField).orElse(null);
    Object rejectedValue = Optional.ofNullable(ex.getFieldError()).map(FieldError::getRejectedValue).orElse(null);
    String message;

    if ("contactEmail".equals(field)) {
        message = String.format("Invalid email format: [%s]. Please enter a valid email address (example@example.com) ", rejectedValue);
    } else if ("phoneNumber".equals(field)) {
        message = String.format("Invalid phone number format: [%s]. Please enter a valid phone number format: +XX YYY ZZZ ZZZ", rejectedValue);
    } else {
        message = String.format("Bad request for field: [%s] with wrong value: [%s].", field, rejectedValue);
    }

    log.error(message, ex);

    ModelAndView modelAndView = new ModelAndView("error");
    modelAndView.addObject("errorMessage", message);

    return modelAndView;
}
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.IM_USED)
//    public ModelAndView handlerException (ConstraintViolationException ex){
//        String message = "Ten adres email już istnieje w bazie, spróbuj użyć innego: [%s]".formatted(ex.getMessage());
//        log.error(message,ex);
//        ModelAndView modelAndView = new ModelAndView("error");
//        modelAndView.addObject("errorMessage",message);
//        return modelAndView;
//
//    }

}
