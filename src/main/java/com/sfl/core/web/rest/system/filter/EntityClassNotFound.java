package com.sfl.core.web.rest.system.filter;


public class EntityClassNotFound extends RuntimeException {
    private static final String ERR_MSG = "Entity Class with the name %s not found!";

    public EntityClassNotFound(String value) {
        super(String.format(ERR_MSG, value));
    }
}
