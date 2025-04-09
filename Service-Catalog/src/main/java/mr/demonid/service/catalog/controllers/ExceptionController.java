package mr.demonid.service.catalog.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.exceptions.CatalogException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
@Log4j2
public class ExceptionController {

    @ExceptionHandler(CatalogException.class)
    public ResponseEntity<Map<String, Object>> catalogException(CatalogException e, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", e.getMessage());
        body.put("path", request.getRequestURI());
        log.error("Ошибка: {}", body);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
