package org.oshepel.api.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.InvalidJsonException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class JsonUtils {

    public static <T> List<T> readList(String str, Class<T> type) {
        return readList(str, ArrayList.class, type);
    }

    public static <T> List<T> readList(String json, Class<? extends Collection> type, Class<T> elementType) {
        final ObjectMapper mapper = newMapper();
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(type, elementType));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new InvalidJsonException(json, e);
        }
    }

    public static <T> T readObject(String json, Class<T> type) {
        final ObjectMapper mapper = newMapper();
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new InvalidJsonException(json, e);
        }
    }

    public static String toJson(Object value) {
        final ObjectMapper mapper = newMapper();
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper newMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }
}
