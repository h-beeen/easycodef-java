package io.codef.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String writeValueAsString(EasyCodefResponse response) {
        try {
            return MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw CodefException.of(CodefError.PARSE_ERROR, e);
        }
    }

    public static String writeValueAsString(Map<String, Object> requestBody) {
        try {
            return MAPPER.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw CodefException.of(CodefError.PARSE_ERROR, e);
        }
    }
}
