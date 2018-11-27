package com.fidenz.academy.log;

import com.fidenz.academy.services.InvalidListException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
public class ExeptionHandler {

    private Logger log = Logger.getLogger(LoggingAspect.class.getName());

    @ExceptionHandler
    public ResponseEntity<Error> handleInvalidList(InvalidListException ile) {
        Error error = new Error(HttpStatus.NOT_FOUND.value(), ile.getMessage(), System.currentTimeMillis());
        log.debug(ile.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleResponesNotFound(HttpClientErrorException e) {
        String message = "Server failed to serialize the response! Message from external API: "+e.getMessage();
        Error error = new Error(HttpStatus.NOT_FOUND.value(), message, System.currentTimeMillis());
        log.debug(message);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleAllErrors(Exception e){
        String message = "Bad request! Message from proxy service: "+e.getMessage();
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), message, System.currentTimeMillis());
        log.debug(message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}