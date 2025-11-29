package com.sample.base_project.common.utils.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.hibernate.LazyInitializationException;

import java.io.Serial;
import java.util.function.Function;

@UtilityClass
public class JsonUtils {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static <T> String toJson(Class<T> clazz, T object) {
        try {
            return OBJECT_MAPPER.writerFor(clazz).writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonUtilException("failed toJson for class " + clazz.getName(), e);
        }
    }

    public static <T> T fromJson(Class<T> clazz, String json) throws JsonProcessingException {
        return OBJECT_MAPPER.readerFor(clazz).readValue(json);
    }

    public static <T> String toJson(TypeReference<T> type, T object) {
        try {
            return OBJECT_MAPPER.writerFor(type).writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonUtilException("failed toJson for type " + type, e);
        }
    }

    public static <T> T fromJson(TypeReference<T> type, String json) throws JsonProcessingException {
        return OBJECT_MAPPER.readerFor(type).readValue(json);
    }

    public static class JsonUtilException extends RuntimeException {
        public JsonUtilException(String msg, Throwable reason) {
            super(msg, reason);
        }
    }
}
