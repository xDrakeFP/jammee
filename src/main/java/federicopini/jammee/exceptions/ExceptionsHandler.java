package federicopini.jammee.exceptions;

import ch.qos.logback.core.status.ErrorStatus;
import federicopini.jammee.DTOs.errors.ErrorsDTO;
import federicopini.jammee.DTOs.errors.ErrorsListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler extends RuntimeException {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handlerNotFound (NotFoundException e){
        return new ErrorsDTO("Elemento non trovato o ID errato", LocalDateTime.now());
    }
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsListDTO handleValidationErrors(ValidationException e) {
        return new ErrorsListDTO(e.getMessage(), LocalDateTime.now(), e.getErrors());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleBadRequest(BadRequestException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AlreadyExistingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleAlreadyExisting(AlreadyExistingException ex){
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }
}
