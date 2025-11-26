package io.codef.api.util;

import io.codef.api.error.CodefException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static io.codef.api.error.CodefError.UNSUPPORTED_ENCODING;

public class URLUtil {

    public static String decode(String content) {
        try {
            return URLDecoder.decode(content, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw CodefException.of(UNSUPPORTED_ENCODING, e.getMessage());
        }
    }
}
