package com.sfl.core.web.rest.system.filter;

public class InvalidFilterStateException extends RuntimeException {
    public InvalidFilterStateException(){}
    public InvalidFilterStateException(String message){ super(message);}
}
