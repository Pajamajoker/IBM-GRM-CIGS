package com.ibm.cigs.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionResponse extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ExceptionResponse.class);
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handler(Exception ex, WebRequest request) {
        logger.error(ex.getMessage());
        String respMessage = "There was an issue with your current conversation!\nPlease try again.";
        return new ResponseEntity<>(respMessage, HttpStatus.BAD_REQUEST);
    }
}
