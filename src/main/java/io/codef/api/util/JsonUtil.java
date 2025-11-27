package io.codef.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REF = new TypeReference<Map<String, Object>>() {};

    public static ObjectMapper mapper() {
        return MAPPER;
    }

    public static String writeValueAsString(EasyCodefResponse response) {
        try {
            return MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw CodefException.of(CodefError.PARSE_ERROR, e);
        }
    }

    public static TypeReference<Map<String, Object>> mapTypeRef() {
        return MAP_TYPE_REF;
    }
}
