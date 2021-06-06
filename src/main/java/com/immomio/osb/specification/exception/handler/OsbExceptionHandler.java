package com.immomio.osb.specification.exception.handler;

import com.immomio.osb.specification.exception.service.ServiceBindingException;
import com.immomio.osb.specification.exception.service.ServicePlanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class OsbExceptionHandler {

    @ExceptionHandler({ServicePlanException.class, ServiceBindingException.class})
    public ResponseEntity<String> handleNotAuthenticatedException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(ServicePlanException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
