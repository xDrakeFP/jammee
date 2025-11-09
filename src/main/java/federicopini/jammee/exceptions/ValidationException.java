package federicopini.jammee.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private List<String> errors;
    public ValidationException(List<String> errors) {
        super("Trovati i seguenti errori nella validazione");
        this.errors = errors;
    }

}
