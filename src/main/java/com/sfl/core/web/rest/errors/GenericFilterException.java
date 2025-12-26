package com.sfl.core.web.rest.errors;
import com.sfl.core.web.rest.customhandler.Constants;
import org.springframework.http.HttpStatus;

public class GenericFilterException extends FilterException {

    public GenericFilterException(Throwable e) {
        super(Constants.BAD_REQUEST_EXCEPTION, HttpStatus.BAD_REQUEST, e);
    }

    public GenericFilterException(String message, int applicationStatusCode, HttpStatus httpStatus) {
        super(message, applicationStatusCode, httpStatus);
    }

}
