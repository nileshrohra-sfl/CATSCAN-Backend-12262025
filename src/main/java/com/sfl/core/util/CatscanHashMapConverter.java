package com.sfl.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class CatscanHashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final Logger log = LoggerFactory.getLogger(CatscanHashMapConverter.class);

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        String properties = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            properties = objectMapper.writeValueAsString(attribute);
        } catch (final JsonProcessingException exeception) {
            log.error(exeception.getMessage(), exeception);
        }
        return properties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        Map<String, Object> properties = null;
        try {
            if (Objects.nonNull(dbData)) {
                ObjectMapper objectMapper = new ObjectMapper();
                properties = objectMapper.readValue(dbData, Map.class);
            }
        } catch (final IOException exeception) {
            log.error(exeception.getMessage(), exeception);
        }
        return properties;
    }

}

