package io.codef.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

public class UrlUtil {

	private UrlUtil() {
	}

	public static String decode(String content) {
		try {
			return URLDecoder.decode(content, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw CodefException.of(CodefError.UNSUPPORTED_ENCODING, e.getMessage());
		}
	}
}
