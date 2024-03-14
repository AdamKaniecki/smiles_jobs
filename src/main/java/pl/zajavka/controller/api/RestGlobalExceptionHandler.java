
package pl.zajavka.controller.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.zajavka.controller.dto.ExceptionMessage;

import java.util.Map;
import java.util.UUID;
    // finalnie użytkownik otrzyma jedynie identyfikator błędu a my będziemy mogli odnaleźć go w swoich logach
    @Slf4j
    @RestControllerAdvice
    @Order(Ordered.HIGHEST_PRECEDENCE) // nadajemy najwyższy priorytet naszej obsłudze błędów
    public class RestGlobalExceptionHandler extends ResponseEntityExceptionHandler  //tu mówimy, że jeśli wpadnie wyjątek i framework
            // będzie go próbował obsłużyć po swojemu to ma
            // go przekierować do mnie po ja chce obsługiwać
            // te wyjątki po swojemu
    {
        private static final Map<Class<?>, HttpStatus> MY_EXCEPTION_STATUS = Map.of(
                ConstraintViolationException.class, HttpStatus.BAD_REQUEST, // jeśli wpadnie ten wyjątek to wtedy zwróć status
                // Bad Request
                EntityNotFoundException.class, HttpStatus.NOT_FOUND         // jeśli wpadnie ten wyjątek to wtedy zwróć status
                // Not Found
        );

        @Override// mogę nadpisać tę metodę(a właściwie dodać swój fragment),
        // bo ma modyfikator dostępu protected z klasy ResponseEntityExceptionHandler,
        // dzięki czemu mogę obsłużyć wyjątki po swojemu (czyli wszczepiam śię we fragment tego frameworka)
        protected ResponseEntity<Object> handleExceptionInternal(
                @NonNull Exception ex, //dodaje adnotację NonNull
                Object body,
                @NonNull HttpHeaders headers, //dodaje adnotację NonNull
                @NonNull HttpStatusCode statusCode, //dodaje adnotację NonNull
                @NonNull WebRequest request) //dodaje adnotację NonNull
        {
            final String errorId = UUID.randomUUID().toString(); //dodać rodzaj identyfikatora UUID(będzie generował losowe
            //identyfikatory błędów)
            log.error("Exception: ID={}, HttpStatus={},", errorId, statusCode, ex); // tu będę miał numer wyjątku, status
            // i wyjątek
//                 return super.handleExceptionInternal(ex, body, headers, statusCode, request); - body zrobię po swojemu
            return super.handleExceptionInternal
                    (       ex,
                            ExceptionMessage.of(errorId), // stworzę klasę z wiadomością i metodą statyczną of()
                            // i przekaże tam pole String errorId
                            headers, statusCode,
                            request);
        }
        //             ta metoda określa, że każdy inny wyjątek nieobsłużony w żadnym miejscu ma być obsłużony tutaj
        @ExceptionHandler(Exception.class)
        public ResponseEntity<?> handle(Exception exception){
            return doHandle(exception, getHttpStatusFromException (exception.getClass()));
        }

        //             metoda wspomagająca metodę doHandle()
        private HttpStatus getHttpStatusFromException (final Class<?> exceptionClass)
        {
            return MY_EXCEPTION_STATUS
                    .getOrDefault(// ta metoda określa, że jeśli wyjątek nie jest zdefiniowany w mojej utworzonej mapie
                            // to zwróć mi Internal Server Error
                            exceptionClass, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // metoda wspomagająca moją główną metodę handleExceptionInternal()
        private ResponseEntity<?> doHandle(Exception exception, HttpStatus status){
            final String errorId = UUID.randomUUID().toString();

            log.error("Exception: ID={}, HttpStatus={},", errorId, status, exception);

            return ResponseEntity
                    .status(status) // określamy status odpowiedzi Http
                    .contentType(MediaType.APPLICATION_JSON) // określamy typ odpowiedzi
                    .body(ExceptionMessage.of(errorId)); // w ciele odpowiedzi otrzymamy numer błędu
        }

    }



