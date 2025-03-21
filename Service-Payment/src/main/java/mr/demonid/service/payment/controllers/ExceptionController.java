package mr.demonid.service.payment.controllers;

import mr.demonid.service.payment.exceptions.BasePaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BasePaymentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBasePaymentException(BasePaymentException e) {
        return "[" + LocalDateTime.now() + "] " + e.getTitle() + ": " + e.getMessage();
    }

}
