package tz;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class TpErrorController {

    @ResponseBody
    @ExceptionHandler(CurrencyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    TpErrorResponse сurrencyNotFoundExceptionHandler(CurrencyNotFoundException exception) {
        return new TpErrorResponse(exception.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(TypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    TpErrorResponse typeNotFoundException(TypeNotFoundException exception) {
        return new TpErrorResponse(exception.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(UnexpectedPathException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    TpErrorResponse UnexpectedPathException(UnexpectedPathException exception) {
        return new TpErrorResponse(exception.getMessage());
    }
}


class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException(String currency) {
        super("could not find currency '" + currency + "' ");
    }
}


class TypeNotFoundException extends RuntimeException {

    public TypeNotFoundException(String type) {
        super("could not find type '" + type + "', try for example BTC or BCH ");
    }
}


class UnexpectedPathException extends RuntimeException {

    public UnexpectedPathException(String path) {
        super("incorrect path '" + path + "', try for example /rates");
    }
}