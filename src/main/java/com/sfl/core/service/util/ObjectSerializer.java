package com.sfl.core.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectSerializer {

    private ObjectSerializer() {
    }

    private static Logger logger= LoggerFactory.getLogger(ObjectSerializer.class);
    private static ObjectMapper objectMapper=new ObjectMapper();
    public static String serialize(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            return "{}";
        }
    }
}

