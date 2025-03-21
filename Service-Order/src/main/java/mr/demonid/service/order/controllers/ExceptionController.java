package mr.demonid.service.order.controllers;

import mr.demonid.service.order.exceptions.BaseOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BaseOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBaseOrderException(BaseOrderException e) {
        return "[" + LocalDateTime.now() + "] " + e.getTitle() + ": " + e.getMessage();
    }

}
