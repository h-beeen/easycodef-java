package io.codef.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

/**
 * URL 디코딩 처리 유틸리티 클래스
 *
 */
public class UrlUtil {

	private UrlUtil() {
	}

	/**
	 * UTF-8 기준 URL 디코딩 수행
	 *
	 * @param content 디코딩할 문자열
	 * @return 디코딩된 문자열
	 * @throws CodefException UTF-8 인코딩을 지원하지 않는 경우 {@link CodefError#UNSUPPORTED_ENCODING}
	 */
	public static String decode(String content) {
		try {
			return URLDecoder.decode(content, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw CodefException.of(CodefError.UNSUPPORTED_ENCODING, e.getMessage());
		}
	}
}
