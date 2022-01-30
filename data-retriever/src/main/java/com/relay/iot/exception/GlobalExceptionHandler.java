package com.relay.iot.exception;

import com.relay.iot.model.Response;
import com.relay.iot.service.LocaleResolverService;
import com.relay.iot.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private TranslationService translationService;
    private LocaleResolverService localeResolverService;

    public GlobalExceptionHandler(TranslationService translationService, LocaleResolverService localeResolverService)
    {
        this.translationService = translationService;
        this.localeResolverService = localeResolverService;
    }

    @ExceptionHandler(InvalidDataException.class)
    protected ResponseEntity<Response> handleInvalidDataException(HttpServletRequest request, InvalidDataException ide) {
        return buildResponseEntity(ide, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    protected ResponseEntity<Response> handleUnAuthorizedException(HttpServletRequest request, UnAuthorizedException uae) {
        return buildResponseEntity(uae, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Response> handleException(HttpServletRequest request, Exception e) {
        return buildResponseEntity(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Response> buildResponseEntity(Exception e, HttpServletRequest request, HttpStatus status) {
        Response response = new Response();
        if (e instanceof EventStreamAppException) {
            EventStreamAppException exception = (EventStreamAppException) e;
            log.error("code = " + (exception.getCode() + ", url = " + request.getRequestURI()), e);
            response.setCode(exception.getCode());
            response.setMessage(translationService.translate(exception.getMessage(), localeResolverService.resolveLocale(request)));
        } else {
            log.error("url = " + request.getRequestURI(), e);
            response.setCode(500);
            response.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, status);
    }
}
